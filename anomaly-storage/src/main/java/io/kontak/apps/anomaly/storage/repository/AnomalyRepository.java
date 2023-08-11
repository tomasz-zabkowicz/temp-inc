package io.kontak.apps.anomaly.storage.repository;

import io.kontak.apps.anomaly.storage.model.Anomaly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, UUID> {

    List<Anomaly> findByThermometer_Id(UUID thermometerId);

    List<Anomaly> findByThermometer_IdAndTimestamp(@Param("thermometerId") UUID thermometerId, @Param("timestamp") Instant timestamp);

    List<Anomaly> findByThermometer_Room_Id(UUID roomId);
}
