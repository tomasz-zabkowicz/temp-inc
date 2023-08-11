package io.kontak.apps.anomaly.detector;

import io.kontak.apps.anomaly.storage.model.Room;
import io.kontak.apps.anomaly.storage.model.TemperatureReading;
import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.service.AnomalyService;
import io.kontak.apps.anomaly.storage.service.TemperatureReadingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TwoFactorAnomalyDetectorUnitTest {

    private TwoFactorAnomalyDetector anomalyDetector;

    @Mock
    private AnomalyService anomalyService;

    @Mock
    private TemperatureReadingService temperatureReadingService;

    @BeforeEach
    public void init()
    {
        anomalyDetector = new TwoFactorAnomalyDetector(temperatureReadingService, anomalyService);
    }

    @Test
    public void testDetectAnomaliesInRecentMeasurementsWithinTimeWindow()
    {
        final List<TemperatureReading> temperatureReadings = temperatureReadings();
        temperatureReadings.get(temperatureReadings.size() - 1).setTemperature(25.4);
        final UUID thermometerId = temperatureReadings.get(0).getThermometer().getId();

        when(temperatureReadingService.findByThermometerAndTimeWindow(any(), any())).thenReturn(temperatureReadings);

        final List<TemperatureReading> anomalyReadings = anomalyDetector.detectAnomaliesInRecentMeasurementsWithinTimeWindow(thermometerId);
        assertEquals("1 anomaly expected!", 1, anomalyReadings.size());
        assertEquals("25.4 temperature expected!", 25.4, anomalyReadings.get(0).getTemperature());
    }

    @Test
    public void testDetectAnomalyInRecentXConsecutiveMeasurements()
    {
        final List<TemperatureReading> temperatureReadings = temperatureReadings();
        final UUID thermometerId = temperatureReadings.get(0).getThermometer().getId();

        when(temperatureReadingService.findLastXByThermometer(thermometerId, 10)).thenReturn(temperatureReadings);

        final Optional<TemperatureReading> anomalyReadingOpt = anomalyDetector.detectAnomalyInRecentXConsecutiveMeasurements(thermometerId);
        assertTrue("Anomaly not found though expected!", anomalyReadingOpt.isPresent());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals("Anomaly temperature expected to be 25.1!", 25.1, anomalyReadingOpt.get().getTemperature());
    }

    private Room room(@SuppressWarnings("SameParameterValue") String id)
    {
        final Room room = new Room();
        room.setId(UUID.fromString(id));
        return room;
    }

    private TemperatureReading temperatureReading(String id, Thermometer thermometer, double temperature)
    {
        final TemperatureReading reading = new TemperatureReading();
        reading.setId(UUID.fromString(id));
        reading.setThermometer(thermometer);
        reading.setTemperature(temperature);
        reading.setTimestamp(Instant.now());
        return reading;
    }

    private List<TemperatureReading> temperatureReadings()
    {
        final Room room = room("20cb1e05-1143-4155-a4d2-00785caaa63b");
        final Thermometer thermometer = thermometer("5e12c8e8-6013-4008-999f-d9569e1a3104", room);
        final List<TemperatureReading> readings = new ArrayList<>();
        readings.add(temperatureReading("687f4613-730e-4e73-8644-7a7e206feb71", thermometer, 19.1));
        readings.add(temperatureReading("471aa9d9-bf30-401f-8846-7f9a35cb5e22", thermometer, 19.2));
        readings.add(temperatureReading("34ce47f7-4390-442f-8adc-d110fc32c517", thermometer, 19.5));
        readings.add(temperatureReading("2698d453-304d-40f0-ae30-02f07c86d500", thermometer, 19.7));
        readings.add(temperatureReading("7c9e2870-b2b9-408d-a7e2-2d4fe09e8c15", thermometer, 19.3));
        readings.add(temperatureReading("3e82179a-ed56-4472-a5bd-5b687de6ae44", thermometer, 25.1));
        readings.add(temperatureReading("9ec2315d-3679-4acb-9259-b0400f9700c5", thermometer, 18.2));
        readings.add(temperatureReading("67459a92-9b97-4f3d-a861-d2401e343bfb", thermometer, 19.1));
        readings.add(temperatureReading("bc94dd6f-be74-4f04-b132-2d2daadfac4d", thermometer, 19.2));
        readings.add(temperatureReading("c60475a9-e8b8-4710-aedb-c250bc06b6a2", thermometer, 18.4));
        return readings;
    }

    private Thermometer thermometer(@SuppressWarnings("SameParameterValue") String id, Room room)
    {
        final Thermometer thermometer = new Thermometer();
        thermometer.setId(UUID.fromString(id));
        thermometer.setRoom(room);
        return thermometer;
    }
}
