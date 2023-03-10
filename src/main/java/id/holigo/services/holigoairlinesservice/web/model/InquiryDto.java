package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.common.model.TripType;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InquiryDto implements Serializable {

    private UUID id;

    private Long userId;

    private String airlinesCode;

    private String originAirportId;

    private AirportDto originAirport;

    private AirportDto destinationAirport;

    private String destinationAirportId;

    private Date departureDate;

    private Date returnDate;

    private TripType tripType;

    private Integer adultAmount;

    private Integer childAmount;

    private Integer infantAmount;

    private String seatClass;

}
