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
public class MedicalDto implements Serializable {

    private String code;

    private String name;

    @JsonProperty("fare")
    private BigDecimal fareAmount;
}
