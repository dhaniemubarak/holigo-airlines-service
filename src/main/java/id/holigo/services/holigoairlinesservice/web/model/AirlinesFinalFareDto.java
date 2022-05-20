package id.holigo.services.holigoairlinesservice.web.model;

import lombok.*;

import java.math.BigDecimal;
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

    private String note;

    private Boolean isBookable;

    private Boolean isIdentityNumberRequired;

    private List<TripDto> trips;
}
