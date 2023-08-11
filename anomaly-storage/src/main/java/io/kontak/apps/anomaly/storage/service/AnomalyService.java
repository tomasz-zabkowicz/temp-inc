package io.kontak.apps.anomaly.storage.service;

import io.kontak.apps.anomaly.storage.model.Anomaly;
import io.kontak.apps.anomaly.storage.model.Room;
import io.kontak.apps.anomaly.storage.model.TemperatureReading;
import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.repository.AnomalyRepository;
import io.kontak.apps.anomaly.storage.repository.RoomRepository;
import io.kontak.apps.anomaly.storage.repository.ThermometerRepository;
import io.kontak.apps.event.AnomalyRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnomalyService extends BaseTemperatureReadingService {

    private final AnomalyRepository anomalyRepository;

    public AnomalyService(AnomalyRepository anomalyRepository, RoomRepository roomRepository, ThermometerRepository thermometerRepository)
    {
        super(roomRepository, thermometerRepository);
        this.anomalyRepository = anomalyRepository;
    }

    public List<Anomaly> findByRoom(UUID roomId)
    {
        return anomalyRepository.findByThermometer_Room_Id(roomId);
    }

    public List<Anomaly> findByThermometer(UUID thermometerId)
    {
        return anomalyRepository.findByThermometer_Id(thermometerId);
    }

    public List<Anomaly> persistAsAnomalies(List<TemperatureReading> readings)
    {
        return readings.stream().map(this::persistAsAnomaly).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    public Optional<Anomaly> persistAsAnomaly(TemperatureReading reading)
    {
        final UUID thermometerId = reading.getThermometer().getId();
        final List<Anomaly> anomaliesMatchingReading = anomalyRepository.findByThermometer_IdAndTimestamp(thermometerId, reading.getTimestamp());
        if (anomaliesMatchingReading.isEmpty()) {
            final Room room = findOrCreateRoom(reading.getThermometer().getRoom().getId());
            final Thermometer thermometer = findOrCreateThermometer(thermometerId, room);
            final Anomaly anomaly = Anomaly.builder().thermometer(thermometer).temperature(reading.getTemperature()).timestamp(reading.getTimestamp()).build();
            return Optional.of(anomalyRepository.save(anomaly));
        }
        return Optional.empty();
    }

    public List<AnomalyRecord> toRecords(List<Anomaly> anomalies)
    {
        return anomalies.stream()
            .map(anomaly -> new AnomalyRecord(anomaly.getTemperature(), anomaly.getThermometer().getRoom().getId().toString(),
                anomaly.getThermometer().getId().toString(), anomaly.getTimestamp()))
            .collect(Collectors.toList());
    }
}
