package id.holigo.services.holigoairlinesservice.web.mappers;


import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseFareDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AirlinesFinalFareTripMapperDecorator
        implements AirlinesFinalFareTripMapper {
    private AirlinesFinalFareTripMapper airlinesFinalFareTripMapper;

    private AirlinesFinalFareTripItineraryMapper airlinesFinalFareTripItineraryMapper;


    @Autowired
    public void setAirlinesFinalFareTripMapper(AirlinesFinalFareTripMapper airlinesFinalFareTripMapper) {
        this.airlinesFinalFareTripMapper = airlinesFinalFareTripMapper;
    }

    @Autowired
    public void setAirlinesFinalFareTripItineraryMapper(AirlinesFinalFareTripItineraryMapper airlinesFinalFareTripItineraryMapper) {
        this.airlinesFinalFareTripItineraryMapper = airlinesFinalFareTripItineraryMapper;
    }

    @Override
    public AirlinesFinalFareTrip airlinesAvailabilityToAirlinesFinalFareTrip(AirlinesAvailability airlinesAvailability) {
        AirlinesFinalFareTrip airlinesFinalFareTrip = this.airlinesFinalFareTripMapper
                .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability);
        List<AirlinesFinalFareTripItinerary> itineraries = airlinesAvailability.getItineraries().stream()
                .map(airlinesFinalFareTripItineraryMapper::airlinesAvailabilityItineraryToAirlinesFinalFareTripItinerary).toList();
        itineraries.forEach(airlinesFinalFareTrip::addToItineraries);
        return airlinesFinalFareTrip;
    }

    @Override
    public AirlinesAvailabilityDto airlinesFinalFareTripToAirlinesAvailabilityDto(AirlinesFinalFareTrip airlinesFinalFareTrip) {
        return this.airlinesFinalFareTripMapper.airlinesFinalFareTripToAirlinesAvailabilityDto(airlinesFinalFareTrip);
    }

}
