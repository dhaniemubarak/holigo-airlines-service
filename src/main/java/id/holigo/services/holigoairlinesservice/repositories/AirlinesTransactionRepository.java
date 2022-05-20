package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesTransactionRepository extends JpaRepository<AirlinesTransaction, UUID> {
}
