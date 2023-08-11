package io.kontak.apps.temperature.analytics.api.controller;

import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.service.ThermometerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/thermometer")
public class ThermometerController {

    private final ThermometerService thermometerService;

    public ThermometerController(ThermometerService thermometerService)
    {
        this.thermometerService = thermometerService;
    }

    @GetMapping(params = {"anomaliesThreshold"})
    public List<Thermometer> getByRoom(@RequestParam("anomaliesThreshold") Long anomaliesThreshold)
    {
        return thermometerService.findByAnomaliesThreshold(anomaliesThreshold);
    }
}
