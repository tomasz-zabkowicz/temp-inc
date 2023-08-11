package io.kontak.apps.anomaly.detector.config;

import io.kontak.apps.anomaly.detector.AnomalyDetector;
import io.kontak.apps.anomaly.detector.TemperatureMeasurementsListener;
import io.kontak.apps.event.AnomalyRecord;
import io.kontak.apps.event.TemperatureReadingRecord;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public Function<KStream<String, TemperatureReadingRecord>, KStream<String, List<AnomalyRecord>>> anomalyDetectorProcessor(AnomalyDetector anomalyDetector) {
        return new TemperatureMeasurementsListener(anomalyDetector);
    }

}
