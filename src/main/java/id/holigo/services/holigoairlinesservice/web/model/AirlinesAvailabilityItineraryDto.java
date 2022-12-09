package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
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

    private String subclass;

    private Integer seatAvailable;

    private BigDecimal normalFare;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;

    private BigDecimal fareAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal adultRates;

    private BigDecimal childRates;

    private BigDecimal infantRates;

    private BigDecimal basicRates;

    private String selectedId;

    private Boolean isPriceInclude;

    private String imageUrl;

}
