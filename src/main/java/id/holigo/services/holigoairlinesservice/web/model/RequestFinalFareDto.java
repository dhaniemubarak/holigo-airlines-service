package id.holigo.services.holigoairlinesservice.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RequestFinalFareDto implements Serializable {

    private List<TripDto> trips;
}
