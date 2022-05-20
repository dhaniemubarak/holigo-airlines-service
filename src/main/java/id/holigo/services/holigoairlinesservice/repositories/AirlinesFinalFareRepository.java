package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesFinalFareRepository extends JpaRepository<AirlinesFinalFare, UUID> {
}
