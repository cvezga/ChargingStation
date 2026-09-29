package tech.cvezga.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.cvezga.chargingstation.entity.ChargeEvent;
import tech.cvezga.chargingstation.entity.ChargeReservation;

public interface ChargeReservationRepository extends JpaRepository<ChargeReservation, Long> {

}
