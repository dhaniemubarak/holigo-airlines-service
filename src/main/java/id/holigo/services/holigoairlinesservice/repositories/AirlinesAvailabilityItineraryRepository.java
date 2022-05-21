package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesAvailabilityItineraryRepository extends JpaRepository<AirlinesAvailabilityItinerary, UUID> {
}
