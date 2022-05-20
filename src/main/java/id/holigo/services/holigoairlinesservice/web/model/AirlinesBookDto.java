package id.holigo.services.holigoairlinesservice.web.model;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AirlinesBookDto implements Serializable {

    private UUID fareId;

    private ContactPersonDto contactPerson;

    private List<PassengerDto> passengers;

}
