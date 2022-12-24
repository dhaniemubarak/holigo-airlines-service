package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties
public class ResponseFareDto implements Serializable {

    private String error_code;
    private String error_msg;
    private String mmid;
    private String ac;
    private String org;
    private String des;
    private String flight;
    private String tgl_dep;
    private String tgl_ret;
    private String adt;
    private String chd;
    private String inf;
    private String bookStat;
    private String bookNote;
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount;
    @JsonProperty("TotalAmountRet")
    private BigDecimal totalAmountRet;
    @JsonProperty("NTA")
    private BigDecimal ntaAmount;
    @JsonProperty("NTARet")
    private BigDecimal ntaAmountRet;
    private RetrossFinalFareDto schedule;
    private String cabin;
    private String trxId;
    private List<String> rules;
}
