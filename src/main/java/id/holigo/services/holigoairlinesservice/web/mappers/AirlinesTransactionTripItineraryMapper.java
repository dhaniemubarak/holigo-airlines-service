package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripItinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AirlinesTransactionTripItineraryMapper {
    @Mapping(target = "originAirportId", ignore = true)
    @Mapping(target = "destinationAirportId", ignore = true)
    @Mapping(target = "pnr", ignore = true)
    @Mapping(target = "leg", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "id", ignore = true)
    AirlinesTransactionTripItinerary airlinesFinalFareItineraryToAirlinesTripItinerary(AirlinesFinalFareTripItinerary airlinesFinalFareTripItinerary);
}
