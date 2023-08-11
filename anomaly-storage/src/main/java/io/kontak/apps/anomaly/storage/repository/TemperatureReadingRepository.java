package io.kontak.apps.anomaly.storage.repository;

import io.kontak.apps.anomaly.storage.model.TemperatureReading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TemperatureReadingRepository extends JpaRepository<TemperatureReading, UUID> {

    List<TemperatureReading> findByThermometer_IdAndTimestampAfter(@Param("thermometerId") UUID thermometerId, @Param("timestamp") Instant timestamp);

    List<TemperatureReading> findByThermometer_IdOrderByTimestampDesc(@Param("thermometerId") UUID thermometerId, Pageable pageable);
}
