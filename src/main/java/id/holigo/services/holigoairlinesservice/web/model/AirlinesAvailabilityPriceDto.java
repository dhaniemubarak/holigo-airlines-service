package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlinesAvailabilityPriceDto implements Serializable {

    private BigDecimal normalFare;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;

    private BigDecimal fareAmount;

    private String subclass;

    private Integer seatAvailable;

    private String selectedId;
}
