package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesAvailabilityDto {

    private String airlinesCode;

    private String airlinesName;

    private String flightNumber;

    private String originAirportId;

    private String destinationAirportId;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer duration;

    private Integer transit;

    private String imageUrl;

    private AirlinesAvailabilityFareDto fare;

    private List<AirlinesAvailabilityTagsDto> tags = new ArrayList<>();
}
