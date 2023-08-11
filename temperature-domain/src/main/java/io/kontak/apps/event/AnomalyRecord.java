package io.kontak.apps.event;

import java.time.Instant;

public record AnomalyRecord(double temperature, String roomId, String thermometerId, Instant timestamp) {
}
