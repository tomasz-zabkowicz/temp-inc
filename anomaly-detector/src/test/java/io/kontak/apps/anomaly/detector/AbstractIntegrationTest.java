package io.kontak.apps.anomaly.detector;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = AnomalyDetectorApplication.class)
@Testcontainers
public class AbstractIntegrationTest {

    public final static KafkaContainer kafkaContainer;

    public final static PostgreSQLContainer postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:14.8-alpine"))
            .withDatabaseName("temp-inc")
            .withUsername("sa")
            .withPassword("Kaktus1");
        postgresContainer.start();
        kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"));
        kafkaContainer.start();
    }

    @DynamicPropertySource
    static void datasourceConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.stream.binders.kafka.environment.spring.cloud.stream.kafka.streams.binder.brokers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }

}
