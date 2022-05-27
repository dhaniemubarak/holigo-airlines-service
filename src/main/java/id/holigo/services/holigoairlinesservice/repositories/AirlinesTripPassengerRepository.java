package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTripPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesTripPassengerRepository extends JpaRepository<AirlinesTripPassenger, UUID> {

}
