package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(AirlinesAvailabilityFareMapperDecorator.class)
public interface AirlinesAvailabilityFareMapper {

    @Mapping(target = "nraAmount", ignore = true)
    @Mapping(target = "subclass", source = "subClass")
    @Mapping(target = "selectedId", source = "selectedIdDep")
    @Mapping(target = "seatAvailable", source = "seatAvb")
    @Mapping(target = "ntaAmount", source = "nta")
    @Mapping(target = "fareAmount", source = "totalFare")
    AirlinesAvailabilityFareDto retrossFareToAirlinesAvailabilityFareDto(RetrossFareDto fare);

    AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "airlinesAvailability", ignore = true)
    AirlinesAvailabilityFare airlinesAvailabilityFareDtoToAirlinesAvailabilityFare(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto);
}
