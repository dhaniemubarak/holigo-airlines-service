package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.stream.Collectors;

@Slf4j
public abstract class AirlinesFinalFareMapperDecorator implements AirlinesFinalFareMapper {

    private AirlinesFinalFareMapper airlinesFinalFareMapper;


    private AirlinesFinalFareTripMapper airlinesFinalFareTripMapper;

    @Autowired
    public void setAirlinesFareMapper(AirlinesFinalFareMapper airlinesFinalFareMapper) {
        this.airlinesFinalFareMapper = airlinesFinalFareMapper;
    }

    @Autowired
    public void setAirlinesFinalFareTripMapper(AirlinesFinalFareTripMapper airlinesFinalFareTripMapper) {
        this.airlinesFinalFareTripMapper = airlinesFinalFareTripMapper;
    }

    @Override
    public AirlinesFinalFareDto airlinesFinalFareToAirlinesFinalFareDto(AirlinesFinalFare airlinesFinalFare) {
        AirlinesFinalFareDto airlinesFinalFareDto = this.airlinesFinalFareMapper.airlinesFinalFareToAirlinesFinalFareDto(airlinesFinalFare);

        airlinesFinalFareDto.setTrips(airlinesFinalFare.getTrips().stream().map(airlinesFinalFareTripMapper::airlinesFinalFareTripToAirlinesFinalFareTripDto).collect(Collectors.toList()));
        return airlinesFinalFareDto;
    }

    @Override
    public AirlinesTransaction airlinesFinalFareToAirlinesTransaction(AirlinesFinalFare airlinesFinalFare) {
        return this.airlinesFinalFareMapper.airlinesFinalFareToAirlinesTransaction(airlinesFinalFare);
    }
}
