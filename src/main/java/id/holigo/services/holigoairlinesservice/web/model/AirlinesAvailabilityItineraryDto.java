package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AirlinesAvailabilityItineraryDto implements Serializable {

    private String airlinesCode; //

    private String airlinesName; //

    private String flightNumber;

    private AirportDto originAirport; //

    private String originAirportId; //

    private AirportDto destinationAirport; //

    private String destinationAirportId; //

    private LocalDate departureDate; //

    private LocalTime departureTime; //

    private LocalDate arrivalDate; //

    private LocalTime arrivalTime; //

    private Integer duration; //

    private Integer transit; //

    private Integer leg;

    private String imageUrl; //

}
