package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlinesFinalFareTripDto implements Serializable {
    private UUID id;
    private String airlinesCode;
    private List<AirlinesFinalFareTripItineraryDto> itineraries = new ArrayList<>();
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
    private Integer adultAmount;
    private Integer childAmount;
    private Integer infantAmount;

    private Boolean isBookable;
    private Boolean isInternational;
    private Boolean isPriceIncluded;

    private Boolean isIdentityNumberRequired;
    private BigDecimal fareAmount;
    private BigDecimal hpAmount;

    private JsonNode baggage;

    private JsonNode meal;

    private JsonNode medical;

    private JsonNode seat;
}
