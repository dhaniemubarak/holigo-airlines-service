package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.IssuedError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuedErrorRepository extends JpaRepository<IssuedError, Long> {
}
