package id.holigo.services.holigoairlinesservice.repositories;

import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface InquiryRepository extends JpaRepository<Inquiry, UUID> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM inquiries " +
                    "WHERE airlines_code = :airlinesCode " +
                    "AND origin_airport_id = :originAirportId " +
                    "AND destination_airport_id= :destinationAirportId " +
                    "AND departure_date=:departureDate " +
                    "AND (:returnDate is null OR return_date=:returnDate) " +
                    "AND trip_type=:tripType " +
                    "AND adult_amount=:adultAmount " +
                    "AND child_amount=:childAmount " +
                    "AND infant_amount=:infantAmount " +
                    "AND seat_class=:seatClass LIMIT 1")
    Optional<Inquiry> getInquiry(
            @Param("airlinesCode") String airlinesCode,
            @Param("originAirportId") String originAirportId,
            @Param("destinationAirportId") String destinationAirportId,
            @Param("departureDate") String departureDate,
            @Param("returnDate") String returnDate,
            @Param("tripType") String tripType,
            @Param("adultAmount") Integer adultAmount,
            @Param("childAmount") Integer childAmount,
            @Param("infantAmount") Integer infantAmount,
            @Param("seatClass") String seatClass);
}
