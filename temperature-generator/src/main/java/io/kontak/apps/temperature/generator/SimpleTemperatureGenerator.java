package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReadingRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

    private static final double ANOMALY_PROBABILITY = 0.10;

    private static final double ANOMALY_TEMP_MAX = 30.0;

    private static final double ANOMALY_TEMP_MIN = 10.0;

    private static final double STANDARD_TEMP_MAX = 22.0;

    private static final double STANDARD_TEMP_MIN = 18.0;

    private final Random random = new Random();

    private final List<Thermometer> thermometers = Arrays.asList(
        new Thermometer("5e12c8e8-6013-4008-999f-d9569e1a3104", "20cb1e05-1143-4155-a4d2-00785caaa63b"),
        new Thermometer("687f4613-730e-4e73-8644-7a7e206feb71", "20cb1e05-1143-4155-a4d2-00785caaa63b"),
        new Thermometer("471aa9d9-bf30-401f-8846-7f9a35cb5e22", "7c9e2870-b2b9-408d-a7e2-2d4fe09e8c15"),
        new Thermometer("34ce47f7-4390-442f-8adc-d110fc32c517", "3e82179a-ed56-4472-a5bd-5b687de6ae44"),
        new Thermometer("2698d453-304d-40f0-ae30-02f07c86d500", "3e82179a-ed56-4472-a5bd-5b687de6ae44"));

    @Override
    public List<TemperatureReadingRecord> generate()
    {
        return List.of(generateSingleReading());
    }

    private TemperatureReadingRecord generateSingleReading()
    {
        final Thermometer thermometer = randomEntry(thermometers);
        final double temperature = randomTemperature();
        return new TemperatureReadingRecord(temperature, thermometer.roomId(), thermometer.id(), Instant.now());
    }

    private boolean randomBoolean(@SuppressWarnings("SameParameterValue") double probability)
    {
        return random.nextDouble() <= probability;
    }

    private <T> T randomEntry(List<T> entries)
    {
        return entries.get(random.nextInt(entries.size()));
    }

    private double randomTemperature()
    {
        final double temperature;
        if (randomBoolean(ANOMALY_PROBABILITY)) {
            temperature = random.nextDouble(ANOMALY_TEMP_MIN, ANOMALY_TEMP_MAX);
        } else {
            temperature = random.nextDouble(STANDARD_TEMP_MIN, STANDARD_TEMP_MAX);
        }
        return Math.round(temperature * 100.0) / 100.0;
    }

    private record Thermometer(String id, String roomId) {

    }
}
