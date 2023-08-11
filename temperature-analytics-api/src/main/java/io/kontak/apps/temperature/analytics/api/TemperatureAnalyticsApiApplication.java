package io.kontak.apps.temperature.analytics.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"io.kontak.apps.temperature.analytics.api", "io.kontak.apps.anomaly.storage"})
@EntityScan("io.kontak.apps.anomaly.storage")
@EnableJpaRepositories("io.kontak.apps.anomaly.storage")
public class TemperatureAnalyticsApiApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(TemperatureAnalyticsApiApplication.class, args);
    }
}
