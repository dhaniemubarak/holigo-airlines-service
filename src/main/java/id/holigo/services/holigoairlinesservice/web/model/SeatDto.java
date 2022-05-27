package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SeatDto implements Serializable {

    private String code;

    @JsonProperty("fare")
    private BigDecimal fareAmount;
}
