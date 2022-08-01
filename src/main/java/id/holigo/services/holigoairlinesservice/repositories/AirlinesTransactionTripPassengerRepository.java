package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AirlinesTransactionTripPassengerRepository extends JpaRepository<AirlinesTransactionTripPassenger, UUID> {

    List<AirlinesTransactionTripPassenger> findAllByTripId(UUID id);

}
