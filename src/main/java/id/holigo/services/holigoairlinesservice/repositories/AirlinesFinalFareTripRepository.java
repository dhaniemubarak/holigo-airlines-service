package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesFinalFareTripRepository extends JpaRepository<AirlinesFinalFareTrip, UUID> {
}
