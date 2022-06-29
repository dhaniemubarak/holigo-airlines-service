package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTrip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AirlinesTransactionTripMapper {

    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "passengers", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "itineraries", ignore = true)
    AirlinesTransactionTrip airlinesFinalFareTripToAirlinesTransactionTrip(AirlinesFinalFareTrip airlinesFinalFareTrip);
}
