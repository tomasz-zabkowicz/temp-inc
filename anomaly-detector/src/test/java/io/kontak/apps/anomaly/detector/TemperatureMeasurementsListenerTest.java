package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.TemperatureReadingRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<List> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
            List.class
        );
             TestKafkaProducer<TemperatureReadingRecord> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            final List<Double> anomalyTemps = Arrays.asList(25.1, 25.4);
            final List<TemperatureReadingRecord> temperatureReadings = temperatureReadings(anomalyTemps);
            temperatureReadings.forEach(reading -> producer.produce(reading.thermometerId(), reading));
            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> anomalyTemps.contains(extractTemperature(r))),
                    Duration.ofSeconds(50)
            );
        }
    }

    private double extractTemperature(ConsumerRecord<String, List> consumerRecord){
        return (Double)((LinkedHashMap)consumerRecord.value().get(0)).get("temperature");
    }

    private List<TemperatureReadingRecord> temperatureReadings(List<Double> anomalyTemps){
        final List<TemperatureReadingRecord> readings = new ArrayList<>();
        readings.add(new TemperatureReadingRecord(19.1, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.2, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.5, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.7, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.3, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(anomalyTemps.get(0), "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(18.2, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.1, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(19.2, "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        readings.add(new TemperatureReadingRecord(anomalyTemps.get(1), "20cb1e05-1143-4155-a4d2-00785caaa63b", "5e12c8e8-6013-4008-999f-d9569e1a3104", Instant.now()));
        return readings;
    }
}
