package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Passenger;
import id.holigo.services.holigoairlinesservice.web.model.PassengerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {IdentityCardMapper.class, PassportMapper.class})
public interface PassengerMapper {

    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Passenger passengerDtoToPassenger(PassengerDto passengerDto);

    @Mapping(target = "seatCode", ignore = true)
    @Mapping(target = "baggageCode", ignore = true)
    PassengerDto passengerToPassengerDto(Passenger passenger);
}
