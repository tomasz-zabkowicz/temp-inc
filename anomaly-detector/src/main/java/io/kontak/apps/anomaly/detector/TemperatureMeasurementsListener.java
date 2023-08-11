package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.AnomalyRecord;
import io.kontak.apps.event.TemperatureReadingRecord;
import org.apache.kafka.streams.kstream.KStream;

import java.util.List;
import java.util.function.Function;

public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReadingRecord>, KStream<String, List<AnomalyRecord>>> {

    private final AnomalyDetector anomalyDetector;

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector)
    {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, List<AnomalyRecord>> apply(KStream<String, TemperatureReadingRecord> events)
    {
        return events.mapValues((temperatureReading) -> anomalyDetector.apply(List.of(temperatureReading)))
            .filter((s, anomalies) -> !anomalies.isEmpty())
            .selectKey((s, anomalies) -> anomalies.get(0).thermometerId());
    }
}
