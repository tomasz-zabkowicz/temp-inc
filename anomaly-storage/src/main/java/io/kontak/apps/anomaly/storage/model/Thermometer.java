package io.kontak.apps.anomaly.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "thermometer")
public class Thermometer {

    @Id
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    private String name;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "ROOM_ID", foreignKey = @ForeignKey(name = "FK___THERMOMETER___ROOM"), nullable = false)
    private Room room;
}
