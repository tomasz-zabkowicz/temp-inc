package io.kontak.apps.anomaly.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseTemperatureReading {

    @NotNull
    @Column(nullable = false)
    private double temperature;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "THERMOMETER_ID", foreignKey = @ForeignKey(name = "FK___TEMPERATURE_READING___THERMOMETER"), nullable = false)
    private Thermometer thermometer;

    @NotNull
    @Column(nullable = false)
    private Instant timestamp;
}
