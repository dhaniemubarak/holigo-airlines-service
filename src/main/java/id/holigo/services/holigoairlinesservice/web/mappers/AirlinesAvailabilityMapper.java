package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossDepartureDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;


@Mapper
@DecoratedWith(AirlinesAvailabilityMapperDecorator.class)
public interface AirlinesAvailabilityMapper {

    AirlinesAvailabilityDto retrossDepartureDtoDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto);

    ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto);
}
