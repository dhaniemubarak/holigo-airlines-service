package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesTransactionTripDtoForUser implements Serializable {
    private String flightNumber;

    private List<AirlinesTransactionTripItineraryDto> itineraries;

    private Integer segment;

    private List<AirlinesTransactionTripPassengerDto> passengers;

    private String airlinesCode;

    private String seatClass;

    private AirportDto originAirport;

    private AirportDto destinationAirport;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;

    private Integer adultAmount;

    private Integer childAmount;

    private Integer infantAmount;

    private Boolean isPriceIncluded;

    private Boolean isInternational;

    private PaymentStatusEnum paymentStatus;

    private OrderStatusEnum orderStatus;

    private BigDecimal fareAmount;

    private BigDecimal adminAmount;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;
}
