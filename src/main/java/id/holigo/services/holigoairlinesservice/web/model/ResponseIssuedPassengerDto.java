package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties
public class ResponseIssuedPassengerDto implements Serializable {
    private String jns;
    private String title;
    private String fn;
    private String ln;
    private String birth;
    private String hp;
    private String noticket;
    private String noticket_ret;
}
