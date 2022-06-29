package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.Passenger;
import id.holigo.services.holigoairlinesservice.web.model.PassengerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {IdentityCardMapper.class, PassportMapper.class})
public interface PassengerMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Passenger passengerDtoToPassenger(PassengerDto passengerDto);

    PassengerDto passengerToPassengerDto(Passenger passenger);
}
