package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlinesFinalFareTripItineraryDto implements Serializable {

    private UUID id;

    private String airlinesCode;

    private String airlinesName;

    private String flightNumber;

    private AirportDto originAirport;

    private AirportDto destinationAirport;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;
}
