package io.kontak.apps.anomaly.storage.service;

import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.repository.ThermometerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThermometerService {

    private final ThermometerRepository thermometerRepository;

    public ThermometerService(ThermometerRepository thermometerRepository)
    {
        this.thermometerRepository = thermometerRepository;
    }

    public List<Thermometer> findByAnomaliesThreshold(long anomaliesThreshold)
    {
        return thermometerRepository.findByAnomaliesThreshold(anomaliesThreshold);
    }
}
