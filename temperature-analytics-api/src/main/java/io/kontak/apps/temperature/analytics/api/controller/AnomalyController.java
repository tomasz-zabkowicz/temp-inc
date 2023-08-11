package io.kontak.apps.temperature.analytics.api.controller;

import io.kontak.apps.anomaly.storage.model.Anomaly;
import io.kontak.apps.anomaly.storage.service.AnomalyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/anomaly")
public class AnomalyController {

    private final AnomalyService anomalyService;

    public AnomalyController(AnomalyService anomalyService)
    {
        this.anomalyService = anomalyService;
    }

    @GetMapping(params = {"roomId"})
    public List<Anomaly> getByRoom(@RequestParam("roomId") String roomId)
    {
        return anomalyService.findByRoom(UUID.fromString(roomId));
    }

    @GetMapping(params = {"thermometerId"})
    public List<Anomaly> getByThermometer(@RequestParam("thermometerId") String thermometerId)
    {
        return anomalyService.findByThermometer(UUID.fromString(thermometerId));
    }
}
