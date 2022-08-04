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

    @Mapping(target = "note", ignore = true)
    @Mapping(target = "trips", ignore = true)
    AirlinesFinalFareDto airlinesFinalFareToAirlinesFinalFareDto(AirlinesFinalFare airlinesFinalFare);


    @Mapping(target = "supplierMessage", ignore = true)
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
