package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class ResponseBookInternationalIssuedDto implements Serializable {
    private String error_code;
    private String error_msg;
    private String notrx;
    private String mmid;
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    @JsonProperty("NTA")
    private String nta;
}
