package tech.cvezga.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.cvezga.chargingstation.entity.ChargeReservation;
import tech.cvezga.chargingstation.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

}
