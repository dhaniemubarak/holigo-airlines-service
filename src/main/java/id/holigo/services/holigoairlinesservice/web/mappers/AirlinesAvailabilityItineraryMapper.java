package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityItineraryDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFlightDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesAvailabilityItineraryMapperDecorator.class)
@Mapper
public interface AirlinesAvailabilityItineraryMapper {

    @Mapping(target = "airlinesCode", ignore = true)
    @Mapping(target = "airlinesName", ignore = true)
    @Mapping(target = "departureDate", ignore = true)
    @Mapping(target = "departureTime", ignore = true)
    @Mapping(target = "arrivalDate", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "originAirport", ignore = true)
    @Mapping(target = "destinationAirport", ignore = true)
    @Mapping(target = "originAirportId", source = "std")
    @Mapping(target = "destinationAirportId", source = "sta")
    AirlinesAvailabilityItineraryDto retrossFlightDtoToAirlinesAvailabilityItineraryDto(RetrossFlightDto retrossFlightDto);

    AirlinesAvailabilityItinerary airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto);
}
