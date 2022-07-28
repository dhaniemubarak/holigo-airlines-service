package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.holigoairlinesservice.domain.PassengerTitle;
import id.holigo.services.holigoairlinesservice.domain.PassengerType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter

public class PassengerDto implements Serializable {

    private UUID id;

    private PassengerType type;

    private PassengerTitle title;

    private String name;

    private String phoneNumber;

    private IdentityCardDto identityCard;

    private PassportDto passport;

    private String baggageCode;

    private String seatCode;

    private Date birthDate;
}
