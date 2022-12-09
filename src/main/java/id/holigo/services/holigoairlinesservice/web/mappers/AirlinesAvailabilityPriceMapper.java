package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityPriceDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@DecoratedWith(AirlinesAvailabilityPriceMapperDecorator.class)
@Mapper
public interface AirlinesAvailabilityPriceMapper {

    @Mapping(target = "hpcAmount", ignore = true)
    @Mapping(target = "normalFare", ignore = true)
    @Mapping(target = "hpAmount", ignore = true)
    AirlinesAvailabilityPriceDto airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(
            AirlinesAvailabilityFareDto airlinesAvailabilityFareDto, Long userId);

    @Mapping(target = "hpcAmount", ignore = true)
    @Mapping(target = "hpAmount", ignore = true)
    AirlinesAvailabilityPriceDto airlinesAvailabilityItineraryToAirlinesAvailabilityPriceDto(
            AirlinesAvailabilityItinerary airlinesAvailabilityItineraries, Long userId);
}
