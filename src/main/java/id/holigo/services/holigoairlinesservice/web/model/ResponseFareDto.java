package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResponseFareDto {

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
    @JsonProperty("NTA")
    private BigDecimal ntaAmount;

    private RetrossScheduleDto schedule;
}
