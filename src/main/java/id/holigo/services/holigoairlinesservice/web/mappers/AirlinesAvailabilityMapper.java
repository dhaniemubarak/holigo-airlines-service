package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossDepartureDto;
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
    @Mapping(target = "originAirportId", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "flightNumber", ignore = true)
    @Mapping(target = "fare", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "destinationAirportId", ignore = true)
    @Mapping(target = "departureTime", ignore = true)
    @Mapping(target = "departureDate", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    @Mapping(target = "arrivalDate", ignore = true)
    @Mapping(target = "airlinesName", ignore = true)
    @Mapping(target = "airlinesCode", ignore = true)
    AirlinesAvailabilityDto retrossDepartureDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto, Long userId);

    @Mapping(target = "returns", ignore = true)
    @Mapping(target = "inquiry", ignore = true)
    @Mapping(target = "departures", ignore = true)
    ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto, Long userId);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "itineraries", ignore = true)
    @Mapping(target = "fares", ignore = true)
    AirlinesAvailability airlinesAvailabilityDtoToAirlinesAvailability(AirlinesAvailabilityDto airlinesAvailabilityDto);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "fare", ignore = true)
    AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability);
}
