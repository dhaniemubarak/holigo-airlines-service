package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Getter
@Setter
public class AirlinesAvailabilityItineraryDto implements Serializable {

    private String airlinesCode;

    private String airlinesName;

    private String flightNumber;

    private AirportDto originAirport;

    private String originAirportId;

    private AirportDto destinationAirport;

    private String destinationAirportId;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer duration;

    private Integer transit;

    private Integer leg;

    private String imageUrl;

}
