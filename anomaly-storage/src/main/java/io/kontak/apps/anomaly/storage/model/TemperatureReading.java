package io.kontak.apps.anomaly.storage.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "temperature_reading")
public class TemperatureReading extends BaseTemperatureReading {

    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
}
