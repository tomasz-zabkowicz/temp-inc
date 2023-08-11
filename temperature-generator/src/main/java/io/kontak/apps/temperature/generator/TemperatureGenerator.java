package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReadingRecord;

import java.util.List;

public interface TemperatureGenerator {

    List<TemperatureReadingRecord> generate();
}
