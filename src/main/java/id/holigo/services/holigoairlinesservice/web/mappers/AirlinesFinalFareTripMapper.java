package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareTripDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesFinalFareTripMapperDecorator.class)
@Mapper(uses = {AirlinesFinalFareTripItineraryMapper.class, AirportMapper.class})
public interface AirlinesFinalFareTripMapper {

    @Mapping(target = "seat", ignore = true)
    @Mapping(target = "medical", ignore = true)
    @Mapping(target = "meal", ignore = true)
    @Mapping(target = "baggage", ignore = true)
    @Mapping(target = "addons", ignore = true)
    @Mapping(target = "supplierId", ignore = true)
    @Mapping(target = "isPriceIncluded", ignore = true)
    @Mapping(target = "isInternational", ignore = true)
    @Mapping(target = "infantAmount", ignore = true)
    @Mapping(target = "childAmount", ignore = true)
    @Mapping(target = "adultAmount", ignore = true)
    @Mapping(target = "prcAmount", ignore = true)
    @Mapping(target = "prAmount", ignore = true)
    @Mapping(target = "ntaAmount", ignore = true)
    @Mapping(target = "nraAmount", ignore = true)
    @Mapping(target = "mpAmount", ignore = true)
    @Mapping(target = "lossAmount", ignore = true)
    @Mapping(target = "isIdentityNumberRequired", ignore = true)
    @Mapping(target = "isBookable", ignore = true)
    @Mapping(target = "ipcAmount", ignore = true)
    @Mapping(target = "ipAmount", ignore = true)
    @Mapping(target = "hvAmount", ignore = true)
    @Mapping(target = "hpcAmount", ignore = true)
    @Mapping(target = "hpAmount", ignore = true)
    @Mapping(target = "finalFare", ignore = true)
    @Mapping(target = "fareAmount", ignore = true)
    @Mapping(target = "cpAmount", ignore = true)
    @Mapping(target = "adminAmount", ignore = true)
    @Mapping(target = "fares", ignore = true)
    @Mapping(target = "itineraries", ignore = true)
    AirlinesFinalFareTrip airlinesAvailabilityToAirlinesFinalFareTrip(AirlinesAvailability airlinesAvailability);


    @Mapping(target = "baggage", ignore = true)
    @Mapping(target = "meal", ignore = true)
    @Mapping(target = "medical", ignore = true)
    @Mapping(target = "seat", ignore = true)
    AirlinesFinalFareTripDto airlinesFinalFareTripToAirlinesFinalFareTripDto(AirlinesFinalFareTrip airlinesFinalFareTrip);

}
