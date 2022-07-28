package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseFareDto;
import id.holigo.services.holigoairlinesservice.web.model.TripDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesFinalFareMapperDecorator.class)
@Mapper
public interface AirlinesFinalFareMapper {
    @Mapping(target = "tripType", ignore = true)
    @Mapping(target = "isPhoneNumberRequired", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "valueNote", ignore = true)
    @Mapping(target = "prcAmount", ignore = true)
    @Mapping(target = "prAmount", ignore = true)
    @Mapping(target = "nraAmount", ignore = true)
    @Mapping(target = "mpAmount", ignore = true)
    @Mapping(target = "lossAmount", ignore = true)
    @Mapping(target = "isIdentityNumberRequired", ignore = true)
    @Mapping(target = "isBookable", ignore = true)
    @Mapping(target = "ipcAmount", ignore = true)
    @Mapping(target = "ipAmount", ignore = true)
    @Mapping(target = "indexNote", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hvAmount", ignore = true)
    @Mapping(target = "hpcAmount", ignore = true)
    @Mapping(target = "hpAmount", ignore = true)
    @Mapping(target = "adminAmount", ignore = true)
    @Mapping(target = "cpAmount", ignore = true)
    @Mapping(target = "fareAmount", ignore = true)
    AirlinesFinalFare responseFareDtoToAirlinesFinalFare(ResponseFareDto responseFareDto, TripDto tripDto, Long userId);

    @Mapping(target = "note", ignore = true)
    @Mapping(target = "trips", ignore = true)
    AirlinesFinalFareDto airlinesFinalFareToAirlinesFinalFareDto(AirlinesFinalFare airlinesFinalFare);


    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trips", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "paymentStatus", ignore = true)
    @Mapping(target = "expiredAt", ignore = true)
    @Mapping(target = "discountAmount", ignore = true)
    @Mapping(target = "contactPerson", ignore = true)
    AirlinesTransaction airlinesFinalFareToAirlinesTransaction(AirlinesFinalFare airlinesFinalFare);
}
