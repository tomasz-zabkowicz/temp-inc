package io.kontak.apps.anomaly.storage.service;

import io.kontak.apps.anomaly.storage.model.Room;
import io.kontak.apps.anomaly.storage.model.TemperatureReading;
import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.repository.RoomRepository;
import io.kontak.apps.anomaly.storage.repository.TemperatureReadingRepository;
import io.kontak.apps.anomaly.storage.repository.ThermometerRepository;
import io.kontak.apps.event.TemperatureReadingRecord;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TemperatureReadingService extends BaseTemperatureReadingService {

    private final TemperatureReadingRepository temperatureReadingRepository;

    public TemperatureReadingService(TemperatureReadingRepository temperatureReadingRepository, RoomRepository roomRepository,
                                     ThermometerRepository thermometerRepository)
    {
        super(roomRepository, thermometerRepository);
        this.temperatureReadingRepository = temperatureReadingRepository;
    }

    public List<TemperatureReading> findByThermometerAndTimeWindow(UUID thermometerId, Instant timeWindowStart)
    {
        return temperatureReadingRepository.findByThermometer_IdAndTimestampAfter(thermometerId, timeWindowStart);
    }

    public List<TemperatureReading> findLastXByThermometer(UUID thermometerId, int pageSize)
    {
        return temperatureReadingRepository.findByThermometer_IdOrderByTimestampDesc(thermometerId, PageRequest.of(0, pageSize));
    }

    public TemperatureReading persist(TemperatureReadingRecord temperatureReadingRecord)
    {
        final Room room = findOrCreateRoom(UUID.fromString(temperatureReadingRecord.roomId()));
        final Thermometer thermometer = findOrCreateThermometer(UUID.fromString(temperatureReadingRecord.thermometerId()), room);
        final TemperatureReading temperatureReading = createTemperatureReading(temperatureReadingRecord, thermometer);
        return temperatureReadingRepository.save(temperatureReading);
    }

    private TemperatureReading createTemperatureReading(TemperatureReadingRecord temperatureReadingRecord, Thermometer thermometer)
    {
        final TemperatureReading reading = new TemperatureReading();
        reading.setThermometer(thermometer);
        reading.setTemperature(temperatureReadingRecord.temperature());
        reading.setTimestamp(temperatureReadingRecord.timestamp());
        return reading;
    }
}
