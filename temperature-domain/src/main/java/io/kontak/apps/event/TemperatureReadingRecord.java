package io.kontak.apps.event;

import java.time.Instant;

public record TemperatureReadingRecord(double temperature, String roomId, String thermometerId, Instant timestamp) {

}
