package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirlinesAvailabilityRepository extends JpaRepository<AirlinesAvailability, AirlinesScheduleId> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM airlines_availabilities " +
                    "WHERE airlines_code = (:airlinesCode) " +
                    "AND origin_airport_id = (:originAirportId) " +
                    "AND destination_airport_id=(:destinationAirportId) " +
                    "AND departure_date=(:departureDate)")
    List<AirlinesAvailability> getAirlinesAvailability(
            @Param("airlinesCode") String airlinesCode,
            @Param("originAirportId") String originAirportId,
            @Param("destinationAirportId") String destinationAirportId,
            @Param("departureDate") String departureDate);
}
