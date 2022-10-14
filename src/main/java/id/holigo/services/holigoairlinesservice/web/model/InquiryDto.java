package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.common.model.TripType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
