package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripItinerary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesTransactionTripItineraryRepository extends JpaRepository<AirlinesTransactionTripItinerary, UUID> {
}
