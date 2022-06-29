package id.holigo.services.holigoairlinesservice.web.mappers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTrip;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareTripDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AirlinesFinalFareTripMapperDecorator
        implements AirlinesFinalFareTripMapper {
    private AirlinesFinalFareTripMapper airlinesFinalFareTripMapper;

    private AirlinesFinalFareTripItineraryMapper airlinesFinalFareTripItineraryMapper;

    private AirportRepository airportRepository;

    private ObjectMapper objectMapper;

    @Autowired
    public void setAirportRepository(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Autowired
    public void setAirlinesFinalFareTripMapper(AirlinesFinalFareTripMapper airlinesFinalFareTripMapper) {
        this.airlinesFinalFareTripMapper = airlinesFinalFareTripMapper;
    }

    @Autowired
    public void setAirlinesFinalFareTripItineraryMapper(AirlinesFinalFareTripItineraryMapper airlinesFinalFareTripItineraryMapper) {
        this.airlinesFinalFareTripItineraryMapper = airlinesFinalFareTripItineraryMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AirlinesFinalFareTrip airlinesAvailabilityToAirlinesFinalFareTrip(AirlinesAvailability airlinesAvailability, Integer segment) {
        AirlinesFinalFareTrip airlinesFinalFareTrip = this.airlinesFinalFareTripMapper
                .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability, segment);
        airlinesFinalFareTrip.setOriginAirport(airportRepository.getById(airlinesAvailability.getOriginAirportId()));
        airlinesFinalFareTrip.setDestinationAirport(airportRepository.getById(airlinesAvailability.getDestinationAirportId()));
        List<AirlinesFinalFareTripItinerary> itineraries = airlinesAvailability.getItineraries().stream()
                .map(airlinesFinalFareTripItineraryMapper::airlinesAvailabilityItineraryToAirlinesFinalFareTripItinerary).toList();
        itineraries.forEach(airlinesFinalFareTrip::addToItineraries);
        return airlinesFinalFareTrip;
    }

    @Override
    public AirlinesFinalFareTripDto airlinesFinalFareTripToAirlinesFinalFareTripDto(AirlinesFinalFareTrip airlinesFinalFareTrip) {
        AirlinesFinalFareTripDto airlinesFinalFareTripDto = this.airlinesFinalFareTripMapper.airlinesFinalFareTripToAirlinesFinalFareTripDto(airlinesFinalFareTrip);
        try {

            if (airlinesFinalFareTrip.getBaggage() != null) {
                airlinesFinalFareTripDto.setBaggage(objectMapper.readValue(airlinesFinalFareTrip.getBaggage(), JsonNode.class));
            }
            if (airlinesFinalFareTrip.getMeal() != null) {
                airlinesFinalFareTripDto.setMeal(objectMapper.readValue(airlinesFinalFareTrip.getMeal(), JsonNode.class));
            }
            if (airlinesFinalFareTrip.getMedical() != null) {
                airlinesFinalFareTripDto.setMedical(objectMapper.readValue(airlinesFinalFareTrip.getMedical(), JsonNode.class));
            }
            if (airlinesFinalFareTrip.getSeat() != null) {
                airlinesFinalFareTripDto.setSeat(objectMapper.readValue(airlinesFinalFareTrip.getSeat(), JsonNode.class));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return airlinesFinalFareTripDto;
    }

}
