package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseScheduleDto implements Serializable {
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
    private JsonNode schedule;
}
