package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesAvailabilityFareRepository extends JpaRepository<AirlinesAvailabilityFare, UUID> {
}
