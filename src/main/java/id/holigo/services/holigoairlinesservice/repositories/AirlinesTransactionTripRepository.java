package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesTransactionTripRepository extends JpaRepository<AirlinesTransactionTrip, UUID> {

}
