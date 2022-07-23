package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTrip;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripDtoForUser;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesTransactionTripMapperDecorator.class)
@Mapper(uses = PassengerMapper.class)
public interface AirlinesTransactionTripMapper {

    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "passengers", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "itineraries", ignore = true)
    AirlinesTransactionTrip airlinesFinalFareTripToAirlinesTransactionTrip(AirlinesFinalFareTrip airlinesFinalFareTrip);

    AirlinesTransactionTripDtoForUser airlinesTransactionTripToAirlinesTransactionTripDtoForUser(AirlinesTransactionTrip airlinesTransactionTrip);
}
