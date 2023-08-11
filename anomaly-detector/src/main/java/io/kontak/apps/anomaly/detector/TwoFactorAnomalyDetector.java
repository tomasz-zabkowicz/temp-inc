package io.kontak.apps.anomaly.detector;

import io.kontak.apps.anomaly.storage.model.Anomaly;
import io.kontak.apps.anomaly.storage.model.BaseTemperatureReading;
import io.kontak.apps.anomaly.storage.model.TemperatureReading;
import io.kontak.apps.anomaly.storage.service.AnomalyService;
import io.kontak.apps.anomaly.storage.service.TemperatureReadingService;
import io.kontak.apps.event.AnomalyRecord;
import io.kontak.apps.event.TemperatureReadingRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TwoFactorAnomalyDetector implements AnomalyDetector {

    private static final int CONSECUTIVE_READINGS_TO_COMPARE = 10;

    private static final double MAX_READING_TEMP_DIFF_COMPARED_TO_AVG = 5.0;

    private static final int SEARCH_TIME_WINDOW_IN_SEC = 10;

    private final AnomalyService anomalyService;

    private final TemperatureReadingService temperatureReadingService;

    public TwoFactorAnomalyDetector(TemperatureReadingService temperatureReadingService, AnomalyService anomalyService)
    {
        this.temperatureReadingService = temperatureReadingService;
        this.anomalyService = anomalyService;
    }

    @Override
    public List<AnomalyRecord> apply(List<TemperatureReadingRecord> temperatureReadings)
    {
        final TemperatureReadingRecord temperatureReadingRecord = temperatureReadings.get(0);

        final TemperatureReading temperatureReading = temperatureReadingService.persist(temperatureReadingRecord);
        final UUID thermometerId = temperatureReading.getThermometer().getId();

        final Set<TemperatureReading> readingsWithAnomalies = new HashSet<>();
        final Optional<TemperatureReading> anomalyReadingOpt = detectAnomalyInRecentXConsecutiveMeasurements(thermometerId);
        anomalyReadingOpt.ifPresent(readingsWithAnomalies::add);
        readingsWithAnomalies.addAll(detectAnomaliesInRecentMeasurementsWithinTimeWindow(thermometerId));

        if (!readingsWithAnomalies.isEmpty()) {
            final List<Anomaly> persistedAnomalies = anomalyService.persistAsAnomalies(new ArrayList<>(readingsWithAnomalies));
            return anomalyService.toRecords(persistedAnomalies);
        }
        return Collections.emptyList();
    }

    protected List<TemperatureReading> detectAnomaliesInRecentMeasurementsWithinTimeWindow(UUID thermometerId)
    {
        final Instant timeWindowStart = Instant.now().minusSeconds(SEARCH_TIME_WINDOW_IN_SEC);
        final List<TemperatureReading> timeWindowReadings = temperatureReadingService.findByThermometerAndTimeWindow(thermometerId, timeWindowStart);

        if (timeWindowReadings.size() > 3) {
            final Double readingsAvgTemperature = findReadingsAvgTemperature(timeWindowReadings);
            return timeWindowReadings.stream()
                .filter(reading -> reading.getTemperature() >= readingsAvgTemperature + MAX_READING_TEMP_DIFF_COMPARED_TO_AVG)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    protected Optional<TemperatureReading> detectAnomalyInRecentXConsecutiveMeasurements(UUID thermometerId)
    {
        final List<TemperatureReading> lastXReadings = temperatureReadingService.findLastXByThermometer(thermometerId, CONSECUTIVE_READINGS_TO_COMPARE);

        if (lastXReadings.size() == CONSECUTIVE_READINGS_TO_COMPARE) {
            final TemperatureReading maxReading = findReadingWithMaxTemperature(lastXReadings);
            lastXReadings.remove(maxReading);
            final Double nonMaxReadingsAvgTemperature = findReadingsAvgTemperature(lastXReadings);

            if (maxReading.getTemperature() >= nonMaxReadingsAvgTemperature + MAX_READING_TEMP_DIFF_COMPARED_TO_AVG) {
                return Optional.of(maxReading);
            }
        }
        return Optional.empty();
    }

    private TemperatureReading findReadingWithMaxTemperature(List<TemperatureReading> readings)
    {
        //noinspection OptionalGetWithoutIsPresent
        return readings.stream().reduce((reading1, reading2) -> reading1.getTemperature() > reading2.getTemperature() ? reading1 : reading2).get();
    }

    private Double findReadingsAvgTemperature(List<TemperatureReading> readings)
    {
        //noinspection OptionalGetWithoutIsPresent
        return readings.stream().mapToDouble(BaseTemperatureReading::getTemperature).average().getAsDouble();
    }
}
