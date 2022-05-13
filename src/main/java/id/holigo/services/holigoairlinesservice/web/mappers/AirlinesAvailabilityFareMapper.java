package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
@DecoratedWith(AirlinesAvailabilityFareMapperDecorator.class)
public interface AirlinesAvailabilityFareMapper {

    @Mapping(target = "seatAvailable", source = "seatAvb")
    @Mapping(target = "ntaAmount", source = "nta")
    @Mapping(target = "fareAmount", source = "totalFare")
    @Mapping(target = "selectedId", source = "selectedIdDep")
    @Mapping(target = "subclass", source = "subClass")
    AirlinesAvailabilityFareDto retrossFareToAirlinesAvailabilityFareDto(RetrossFareDto fare);

    AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto);
}
