package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripItineraryDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesTransactionTripItineraryMapperDecorator.class)
@Mapper
public interface AirlinesTransactionTripItineraryMapper {
    @Mapping(target = "pnr", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "id", ignore = true)
    AirlinesTransactionTripItinerary airlinesFinalFareItineraryToAirlinesTripItinerary(AirlinesFinalFareTripItinerary airlinesFinalFareTripItinerary);

    AirlinesTransactionTripItineraryDto airlinesTransactionTripItineraryToAirlinesTransactionTripItineraryDto(AirlinesTransactionTripItinerary airlinesTransactionTripItinerary);
}
