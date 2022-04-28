package id.holigo.services.holigoairlinesservice.web.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestScheduleDto {
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
