package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTrip;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripDtoForUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

public abstract class AirlinesTransactionTripMapperDecorator implements AirlinesTransactionTripMapper {

    private AirlinesTransactionTripMapper airlinesTransactionTripMapper;

    private AirlinesTransactionTripPassengerMapper airlinesTransactionTripPassengerMapper;

    private AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper;

    @Autowired
    public void setAirlinesTransactionTripPassengerMapper(AirlinesTransactionTripPassengerMapper airlinesTransactionTripPassengerMapper) {
        this.airlinesTransactionTripPassengerMapper = airlinesTransactionTripPassengerMapper;
    }

    @Autowired
    public void setAirlinesTransactionTripMapper(AirlinesTransactionTripMapper airlinesTransactionTripMapper) {
        this.airlinesTransactionTripMapper = airlinesTransactionTripMapper;
    }

    @Autowired
    public void setAirlinesTransactionTripItineraryMapper(AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper) {
        this.airlinesTransactionTripItineraryMapper = airlinesTransactionTripItineraryMapper;
    }

    @Override
    public AirlinesTransactionTrip airlinesFinalFareTripToAirlinesTransactionTrip(AirlinesFinalFareTrip airlinesFinalFareTrip) {
        return this.airlinesTransactionTripMapper.airlinesFinalFareTripToAirlinesTransactionTrip(airlinesFinalFareTrip);
    }

    @Override
    public AirlinesTransactionTripDtoForUser airlinesTransactionTripToAirlinesTransactionTripDtoForUser(AirlinesTransactionTrip airlinesTransactionTrip) {
        AirlinesTransactionTripDtoForUser airlinesTransactionTripDtoForUser = this.airlinesTransactionTripMapper.airlinesTransactionTripToAirlinesTransactionTripDtoForUser(airlinesTransactionTrip);
        airlinesTransactionTripDtoForUser.setPassengers(airlinesTransactionTrip.getPassengers().stream().map(airlinesTransactionTripPassengerMapper::airlinesTransactionTripPassengerToAirlinesTransactionTripPassengerDto).collect(Collectors.toList()));
        airlinesTransactionTripDtoForUser.setItineraries(airlinesTransactionTrip.getItineraries().stream().map(airlinesTransactionTripItineraryMapper::airlinesTransactionTripItineraryToAirlinesTransactionTripItineraryDto).collect(Collectors.toList()));
        return airlinesTransactionTripDtoForUser;
    }
}
