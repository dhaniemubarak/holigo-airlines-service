package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.web.model.*;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
@DecoratedWith(AirlinesAvailabilityMapperDecorator.class)
public interface AirlinesAvailabilityMapper {

    @Mapping(target = "itineraries", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "transit", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "flightNumber", ignore = true)
    @Mapping(target = "fares", ignore = true)
    @Mapping(target = "fare", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "departureTime", ignore = true)
    @Mapping(target = "departureDate", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    @Mapping(target = "arrivalDate", ignore = true)
    @Mapping(target = "airlinesName", ignore = true)
    @Mapping(target = "airlinesCode", ignore = true)
    AirlinesAvailabilityDto retrossDepartureDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto, InquiryDto inquiryDto);

    @Mapping(target = "returns", ignore = true)
    @Mapping(target = "inquiry", ignore = true)
    @Mapping(target = "departures", ignore = true)
    ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto, InquiryDto inquiryDto);

    @Mapping(target = "destinationAirportId", source = "destinationAirport.id")
    @Mapping(target = "originAirportId", source = "originAirport.id")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "itineraries", ignore = true)
    @Mapping(target = "fares", ignore = true)
    AirlinesAvailability airlinesAvailabilityDtoToAirlinesAvailability(AirlinesAvailabilityDto airlinesAvailabilityDto);

    @Mapping(target = "originAirport", ignore = true)
    @Mapping(target = "destinationAirport", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "fare", ignore = true)
    AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability, Long userId, Integer passengerAmount);
}
