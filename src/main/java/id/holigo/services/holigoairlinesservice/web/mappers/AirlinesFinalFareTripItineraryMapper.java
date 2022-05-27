package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AirlinesFinalFareTripItineraryMapper {

    @Mapping(target = "trip", ignore = true)
    AirlinesFinalFareTripItinerary airlinesAvailabilityItineraryToAirlinesFinalFareTripItinerary(AirlinesAvailabilityItinerary airlinesAvailabilityItinerary);
}
