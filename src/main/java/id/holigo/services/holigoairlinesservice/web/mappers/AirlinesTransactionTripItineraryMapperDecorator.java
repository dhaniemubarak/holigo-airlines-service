package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFareTripItinerary;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripItinerary;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripItineraryDto;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AirlinesTransactionTripItineraryMapperDecorator implements AirlinesTransactionTripItineraryMapper {

    private AirportRepository airportRepository;

    private AirportMapper airportMapper;

    private AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper;

    @Autowired
    public void setAirlinesTransactionTripItineraryMapper(AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper) {
        this.airlinesTransactionTripItineraryMapper = airlinesTransactionTripItineraryMapper;
    }

    @Autowired
    public void setAirportRepository(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Autowired
    public void setAirportMapper(AirportMapper airportMapper) {
        this.airportMapper = airportMapper;
    }

    @Override
    public AirlinesTransactionTripItinerary airlinesFinalFareItineraryToAirlinesTripItinerary(AirlinesFinalFareTripItinerary airlinesFinalFareTripItinerary) {
        return this.airlinesTransactionTripItineraryMapper.airlinesFinalFareItineraryToAirlinesTripItinerary(airlinesFinalFareTripItinerary);
    }

    public AirlinesTransactionTripItineraryDto airlinesTransactionTripItineraryToAirlinesTransactionTripItineraryDto(AirlinesTransactionTripItinerary airlinesTransactionTripItinerary) {
        //        if (airlinesTransactionTripItinerary.getOriginAirport() != null) {
//            airlinesTransactionTripItineraryDto.setOriginAirport(airportMapper.airportToAirportDto(airportRepository.getById(airlinesTransactionTripItinerary.getOriginAirportId())));
//        }

//        if (airlinesTransactionTripItinerary.getDestinationAirport() != null) {
//            airlinesTransactionTripItineraryDto.setDestinationAirport(airportMapper.airportToAirportDto(airportRepository.getById(airlinesTransactionTripItinerary.getDestinationAirportId())));
//        }

        return this.airlinesTransactionTripItineraryMapper
                .airlinesTransactionTripItineraryToAirlinesTransactionTripItineraryDto(airlinesTransactionTripItinerary);
    }
}
