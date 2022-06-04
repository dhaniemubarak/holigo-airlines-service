package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetrossFareDto {

    @JsonProperty("SubClass")
    private String subClass;

    @JsonProperty("SeatAvb")
    private Integer seatAvb;

    @JsonProperty("NTA")
    private BigDecimal nta;

    @JsonProperty("TotalFare")
    private BigDecimal totalFare;

    @JsonProperty("selectedIDdep")
    private String selectedIdDep;

    @JsonProperty("selectedIDret")
    private String selectedIdRet;
}
