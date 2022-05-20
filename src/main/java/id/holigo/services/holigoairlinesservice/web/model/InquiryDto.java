package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {

    private UUID id;

    private String airlinesCode;

    private String originAirportId;

    private String destinationAirportId;

    private Date departureDate;

    private Date returnDate;

    private String tripType;

    private Integer adultAmount;

    private Integer childAmount;

    private Integer infantAmount;

    private String seatClass;

}
