package id.holigo.services.holigoairlinesservice.web.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.UUID;

public class AirlinesAvailabilitiesDto {

    private UUID id;

    private String airlinesName;

    private String originAirportId;

    private String destinationAirportId;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer hpAmount;

    private BigDecimal fareAmount;

    private List<AirlinesAvailabilitiesTagDto> tags;
}
