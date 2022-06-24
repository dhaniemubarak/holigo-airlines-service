package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestFareDto {

    private String rqid;

    private String mmid;

    private String app;

    private String action;

    private String org;

    private String des;

    private String flight;

    private Integer adt;

    private Integer chd;

    private Integer inf;

    private String tgl_dep;

    private String tgl_ret;

    private String acDep;

    @JsonProperty("selectedIDdep")
    private String selectedIdDep;

    private String acRet;

    @JsonProperty("selectedIDret")
    private String selectedIdRet;
}
