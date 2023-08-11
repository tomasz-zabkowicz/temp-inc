package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.AnomalyRecord;
import io.kontak.apps.event.TemperatureReadingRecord;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface AnomalyDetector extends Function<List<TemperatureReadingRecord>, List<AnomalyRecord>> {

}
