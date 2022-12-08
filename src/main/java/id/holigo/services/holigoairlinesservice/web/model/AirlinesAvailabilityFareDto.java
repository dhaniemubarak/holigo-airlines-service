package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirlinesAvailabilityFareDto {

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal fareAmount;

    private String subclass;

    private Integer seatAvailable;

    private String selectedId;

    private Integer leg;
}
