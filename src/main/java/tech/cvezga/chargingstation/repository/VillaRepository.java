package tech.cvezga.chargingstation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.cvezga.chargingstation.entity.Station;
import tech.cvezga.chargingstation.entity.Villa;

public interface VillaRepository extends JpaRepository<Villa, Long> {

}
