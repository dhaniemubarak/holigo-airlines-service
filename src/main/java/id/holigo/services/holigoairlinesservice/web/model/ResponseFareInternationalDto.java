package id.holigo.services.holigoairlinesservice.web.model;

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
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFareInternationalDto implements Serializable {
    private String error_code;
    private String error_msg;
    private String mmid;
    private String org;
    private String des;
    private String flight;
    private String tgl_dep;
    private String tgl_ret;
    private String adt;
    private String chd;
    private String inf;
    private String cabin;
    private String bookStat;
    private String bookNote;
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    @JsonProperty("NTA")
    private BigDecimal ntaAmount;
    private String trxId;
    private List<String> rules;
}
