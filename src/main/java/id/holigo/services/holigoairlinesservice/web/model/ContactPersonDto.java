package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class ContactPersonDto implements Serializable {

    private String name;

    private String phoneNumber;

    private String email;
}
