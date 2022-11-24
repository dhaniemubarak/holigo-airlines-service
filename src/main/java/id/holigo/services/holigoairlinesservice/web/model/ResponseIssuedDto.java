package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class ResponseIssuedDto implements Serializable {
    private String error_code;
    private String error_msg;
    private String notrx;
    private String mmid;
    private String acDep;
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    private String status;
    @JsonProperty("NTA")
    private BigDecimal nta;
    private BigDecimal saldo;
    private List<ResponseIssuedPassengerDto> penumpang;
}
