package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossInternationalFareDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
@DecoratedWith(AirlinesAvailabilityFareMapperDecorator.class)
public interface AirlinesAvailabilityFareMapper {

    @Mapping(target = "nraAmount", ignore = true)
    @Mapping(target = "subclass", source = "subClass")
    @Mapping(target = "selectedId", ignore = true)
    @Mapping(target = "seatAvailable", source = "seatAvb")
    @Mapping(target = "ntaAmount", source = "nta")
    @Mapping(target = "fareAmount", source = "totalFare")
    @Mapping(target = "adultRates", source = "fareDetail.adultRates")
    @Mapping(target = "childRates", source = "fareDetail.childRates")
    @Mapping(target = "infantRates", source = "fareDetail.infantRates")
    @Mapping(target = "basicRates", source = "fareDetail.basicRates")
    AirlinesAvailabilityFareDto retrossFareToAirlinesAvailabilityFareDto(RetrossFareDto fare);

    @Mapping(target = "nraAmount", ignore = true)
    @Mapping(target = "subclass", ignore = true)
    @Mapping(target = "selectedId", ignore = true)
    @Mapping(target = "seatAvailable", source = "seatAvb")
    @Mapping(target = "ntaAmount", source = "nta")
    @Mapping(target = "fareAmount", source = "totalFare")
    @Mapping(target = "adultRates", ignore = true)
    @Mapping(target = "childRates", ignore = true)
    @Mapping(target = "infantRates", ignore = true)
    @Mapping(target = "basicRates", ignore = true)
    AirlinesAvailabilityFareDto retrossInternationalFareToAirlinesAvailabilityFareDto(RetrossInternationalFareDto fare);

    AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFare airlinesAvailabilityFare);

    AirlinesAvailabilityFareDto airlinesAvailabilityItineraryToAirlinesAvailabilityFareDto(AirlinesAvailabilityItinerary airlinesAvailabilityItinerary);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "airlinesAvailability", ignore = true)
    AirlinesAvailabilityFare airlinesAvailabilityFareDtoToAirlinesAvailabilityFare(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto);


}
