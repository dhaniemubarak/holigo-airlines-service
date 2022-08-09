package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlinesFinalFareDto {

    private UUID id;

    private BigDecimal fareAmount;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;

    private String note;

    private Boolean isBookable;

    private Boolean isIdentityNumberRequired;

    private Boolean isPhoneNumberRequired;

    private TripType tripType;

    private List<AirlinesFinalFareTripDto> trips = new ArrayList<>();
}
