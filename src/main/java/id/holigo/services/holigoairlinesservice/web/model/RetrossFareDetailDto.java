package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrossFareDetailDto implements Serializable {
    @JsonProperty("Adult")
    private BigDecimal adultRates;

    @JsonProperty("Child")
    private BigDecimal childRates;

    @JsonProperty("Infant")
    private BigDecimal infantRates;

    @JsonProperty("BasicFare")
    private BigDecimal basicRates;
}
