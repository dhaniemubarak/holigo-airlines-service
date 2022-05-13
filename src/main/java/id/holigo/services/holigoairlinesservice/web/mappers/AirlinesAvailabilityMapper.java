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

    AirlinesAvailabilityDto retrossDepartureDtoDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto);

    ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto);

    @Mapping(target = "fare", ignore = true)
    AirlinesAvailability airlinesAvailabilityDtoToAirlinesAvailability(AirlinesAvailabilityDto airlinesAvailabilityDto);

    @Mapping(target = "fare", ignore = true)
    AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability);
}
