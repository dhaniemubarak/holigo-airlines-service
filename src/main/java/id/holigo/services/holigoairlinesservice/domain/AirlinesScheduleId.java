package id.holigo.services.holigoairlinesservice.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AirlinesScheduleId implements Serializable {

    private String airlinesCode;

    private String airlinesName;

    private String flightNumber;

    private String originAirportId;

    private String destinationAirportId;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private LocalDate arrivalDate;

    private LocalTime arrivalTime;

    private String seatClass;
}
