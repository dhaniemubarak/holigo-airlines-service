package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ResponseBookDto implements Serializable {

    private String error_code;
    private String error_msg;
    private String notrx;
    private String mmid;
    private String acDep;
    @JsonProperty("Timelimit")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Timestamp timelimit;
    private BigDecimal totalAmount;
    @JsonProperty("NTA")
    private String nta;
    @JsonProperty("PNRDep")
    private String pnrDep;
    @JsonProperty("PNRRet")
    private String pnrRet;
}
