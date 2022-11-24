package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityItineraryDto;
import id.holigo.services.holigoairlinesservice.web.model.AirportDto;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFlightDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AirlinesAvailabilityItineraryMapperDecorator implements AirlinesAvailabilityItineraryMapper {

    private Airlines airlines;

    private AirportMapper airportMapper;

    private AirportRepository airportRepository;

    private AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper;

    @Autowired
    public void setAirportRepository(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Autowired
    public void setAirportMapper(AirportMapper airportMapper) {
        this.airportMapper = airportMapper;
    }

    @Autowired
    public void setAirlinesAvailabilityItineraryMapper(AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper) {
        this.airlinesAvailabilityItineraryMapper = airlinesAvailabilityItineraryMapper;
    }

    @Autowired
    public void setAirlines(Airlines airlines) {
        this.airlines = airlines;
    }

    @Override
    public AirlinesAvailabilityItineraryDto retrossFlightDtoToAirlinesAvailabilityItineraryDto(RetrossFlightDto retrossFlightDto, InquiryDto inquiryDto) {
        Map<String, String> airlinesMap = airlines.getAirlines(retrossFlightDto.getFlightNumber().substring(0, 2));
        List<AirportDto> airports = new ArrayList<>();
        airports.add(inquiryDto.getOriginAirport());
        airports.add(inquiryDto.getDestinationAirport());

        AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto = new AirlinesAvailabilityItineraryDto();
        AirportDto originAirport = airports.stream()
                .filter(airport -> retrossFlightDto.getStd().equals(airport.getId()))
                .findFirst().orElse(airportMapper.airportToAirportDto(airportRepository.getById(retrossFlightDto.getStd())));
        AirportDto destinationAirport = airports.stream()
                .filter(airport -> retrossFlightDto.getSta().equals(airport.getId()))
                .findFirst().orElse(airportMapper.airportToAirportDto(airportRepository.getById(retrossFlightDto.getSta())));
        airlinesAvailabilityItineraryDto.setFlightNumber(retrossFlightDto.getFlightNumber());
        airlinesAvailabilityItineraryDto.setOriginAirport(originAirport);
        airlinesAvailabilityItineraryDto.setDestinationAirport(destinationAirport);
        airlinesAvailabilityItineraryDto.setAirlinesCode(airlinesMap.get("code"));
        airlinesAvailabilityItineraryDto.setAirlinesName(airlinesMap.get("name"));
        airlinesAvailabilityItineraryDto.setImageUrl(airlinesMap.get("imageUrl"));
        Timestamp etd = Timestamp.valueOf(retrossFlightDto.getEtd() + ":00");
        Timestamp eta = Timestamp.valueOf(retrossFlightDto.getEta() + ":00");
        airlinesAvailabilityItineraryDto.setDepartureDate(new Date(etd.getTime()));
        airlinesAvailabilityItineraryDto.setDepartureTime(new Time(etd.getTime()));
        airlinesAvailabilityItineraryDto.setArrivalDate(new Date(eta.getTime()));
        airlinesAvailabilityItineraryDto.setArrivalTime(new Time(eta.getTime()));
        if (retrossFlightDto.getTransit() != null && (!retrossFlightDto.getTransit().equals(""))) {
            airlinesAvailabilityItineraryDto.setTransit(Integer.parseInt(retrossFlightDto.getTransit()));
        }
        if (retrossFlightDto.getDuration() != null) {
            airlinesAvailabilityItineraryDto.setDuration(Integer.parseInt(retrossFlightDto.getDuration()));
        }


        return airlinesAvailabilityItineraryDto;
    }

    @Override
    public AirlinesAvailabilityItinerary airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto) {
        return airlinesAvailabilityItineraryMapper.airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(airlinesAvailabilityItineraryDto);
    }
}
