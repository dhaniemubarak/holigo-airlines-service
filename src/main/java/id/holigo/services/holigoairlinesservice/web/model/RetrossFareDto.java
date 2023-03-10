package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class RetrossFareDto {

    @JsonProperty("SubClass")
    private String subClass;

    @JsonProperty("SeatAvb")
    private Integer seatAvb;

    @JsonProperty("NTA")
    private BigDecimal nta = BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP);

    @JsonProperty("TotalFare")
    private BigDecimal totalFare;

    @JsonProperty("selectedIDdep")
    private String selectedIdDep;

    @JsonProperty("selectedIDret")
    private String selectedIdRet;

    @JsonProperty("DetailPrice")
    private RetrossFareDetailDto fareDetail;
}
