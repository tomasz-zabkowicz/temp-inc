package io.kontak.apps.anomaly.storage.repository;

import io.kontak.apps.anomaly.storage.model.Thermometer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThermometerRepository extends JpaRepository<Thermometer, UUID> {

    @Query("select t from Thermometer t where (select count(a) from Anomaly a where a.thermometer.id = t.id) >= :anomaliesThreshold")
    List<Thermometer> findByAnomaliesThreshold(long anomaliesThreshold);
}
