package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTripItinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AirlinesTripItineraryMapper {
    @Mapping(target = "pnr", ignore = true)
    @Mapping(target = "leg", ignore = true)
    @Mapping(target = "trip", ignore = true)
    @Mapping(target = "id", ignore = true)
    AirlinesTripItinerary airlinesFinalFareItineraryToAirlinesTripItinerary(AirlinesFinalFareTripItinerary airlinesFinalFareTripItinerary);
}
