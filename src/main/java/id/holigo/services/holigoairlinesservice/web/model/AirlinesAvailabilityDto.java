package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesAvailabilityDto implements Serializable {

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

    private Integer transit;

    private String imageUrl;

    private String seatClass;

    private Boolean isInternational;

    private AirlinesAvailabilityPriceDto fare;

    private List<AirlinesAvailabilityTagsDto> tags = new ArrayList<>();

    private List<AirlinesAvailabilityItineraryDto> itineraries = new ArrayList<>();

    private List<AirlinesAvailabilityFareDto> fares = new ArrayList<>();
}
