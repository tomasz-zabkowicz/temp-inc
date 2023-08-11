package io.kontak.apps.anomaly.detector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"io.kontak.apps.anomaly.detector", "io.kontak.apps.anomaly.storage"})
@EntityScan("io.kontak.apps.anomaly.storage")
@EnableJpaRepositories("io.kontak.apps.anomaly.storage")
public class AnomalyDetectorApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AnomalyDetectorApplication.class, args);
    }
}
