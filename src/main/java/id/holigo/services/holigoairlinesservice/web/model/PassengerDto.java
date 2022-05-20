package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.holigoairlinesservice.domain.PassengerTitle;
import id.holigo.services.holigoairlinesservice.domain.PassengerType;

import java.io.Serializable;

public class PassengerDto implements Serializable {

    private PassengerType type;

    private PassengerTitle title;

    private String name;

    private String phoneNumber;

    private String identityNumber;
}
