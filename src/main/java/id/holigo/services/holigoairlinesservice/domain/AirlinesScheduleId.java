package id.holigo.services.holigoairlinesservice.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

@Data
public class AirlinesScheduleId implements Serializable {

    private String airlinesCode;

    private String airlinesName;

    private String flightNumber;

    private String originAirportId;

    private String destinationAirportId;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private String seatClass;
}
