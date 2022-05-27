package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTrip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirlinesTripRepository extends JpaRepository<AirlinesTrip, UUID> {

}
