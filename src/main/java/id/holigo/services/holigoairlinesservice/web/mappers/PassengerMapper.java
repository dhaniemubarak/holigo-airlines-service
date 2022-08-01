package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripPassenger;
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

    @Mapping(target = "type", source = "passenger.type")
    @Mapping(target = "title", source = "passenger.title")
    @Mapping(target = "name", source = "passenger.name")
    @Mapping(target = "phoneNumber", source = "passenger.phoneNumber")
    @Mapping(target = "identityCard", source = "passenger.identityCard")
    @Mapping(target = "passport", source = "passenger.passport")
    @Mapping(target = "birthDate", source = "passenger.birthDate")
    PassengerDto airlinesTransactionTripPassengerToPassengerDto(AirlinesTransactionTripPassenger airlinesTransactionTripPassenger);
}
