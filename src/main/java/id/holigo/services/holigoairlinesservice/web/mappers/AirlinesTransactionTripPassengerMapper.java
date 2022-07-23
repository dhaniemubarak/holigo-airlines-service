package id.holigo.services.holigoairlinesservice.web.mappers;


import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripPassenger;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripPassengerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {PassengerMapper.class})
public interface AirlinesTransactionTripPassengerMapper {

    AirlinesTransactionTripPassengerDto airlinesTransactionTripPassengerToAirlinesTransactionTripPassengerDto(AirlinesTransactionTripPassenger airlinesTransactionTripPassenger);

}
