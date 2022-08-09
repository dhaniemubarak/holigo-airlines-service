package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class RequestIssuedDto implements Serializable {
    private String action;

    private String app;

    private String rqid;

    private String mmid;

    private String notrx;
}
