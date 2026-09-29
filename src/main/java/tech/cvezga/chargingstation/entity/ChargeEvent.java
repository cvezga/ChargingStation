package tech.cvezga.chargingstation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="charge_events")
@Getter
@Setter
public class ChargeEvent {

    public enum Type {
        AUTO_START, AUTO_FINISHED, AUTO_CANCELED,
        MANUAL_START, MANUAL_FINISHED, MANUAL_CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ChargeReservation chargeReservation;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private double wattsValue;

}
