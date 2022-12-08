package id.holigo.services.holigoairlinesservice.web.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlinesAvailabilityPriceDto implements Serializable {

    private BigDecimal normalFare;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;

    private BigDecimal fareAmount;

    private String subclass;

    private Integer seatAvailable;

    private String selectedId;
}
