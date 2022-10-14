package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.holigoairlinesservice.domain.Airport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesTransactionTripItineraryDto implements Serializable {

    private String pnr;

    private Integer leg;

    private String airlinesName;

    private String flightNumber;

    private AirportDto originAirport;

    private AirportDto destinationAirport;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private LocalDate arrivalDate;

    private LocalTime arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;
}
