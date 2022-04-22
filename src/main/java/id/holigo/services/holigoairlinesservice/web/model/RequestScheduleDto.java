package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestScheduleDto implements Serializable {
    /**
     * {"rqid":"sSm4ajndIdanf2k274hKSNjshfjhqkej1nRTt","mmid":"mastersip","app":"information","action":"get_schedule","ac":"QZ","org":"TRK","des":"BPN","flight":"O","tgl_dep":"2022-04-26","adt":1,"chd":0,"inf":0,"cabin":"E"}
     */

    private String rqid;

    private String mmid;

    private String app;

    private String ac;

    private String action;

    private String org;

    private String des;

    private String flight;

    private String tgl_dep;

    private String tgl_ret;

    private Integer adt;

    private Integer chd;

    private Integer inf;

    private String cabin;
}
