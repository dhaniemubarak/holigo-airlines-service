package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesAvailabilityDto {

    private UUID id;

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

    private Integer hpAmount;

    private BigDecimal fareAmount;

    private List<AirlinesAvailabilityTagsDto> tags;
}
