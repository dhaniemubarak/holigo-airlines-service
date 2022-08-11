package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.domain.Airport;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityItineraryDto;
import id.holigo.services.holigoairlinesservice.web.model.AirportDto;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFlightDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
        airlinesAvailabilityItineraryDto.setDepartureDate(
                Date.valueOf(LocalDate.parse(retrossFlightDto.getEtd().substring(0, 10))));
        airlinesAvailabilityItineraryDto.setDepartureTime(
                Time.valueOf(LocalTime.parse(retrossFlightDto.getEtd().substring(11, 16))));
        airlinesAvailabilityItineraryDto.setArrivalDate(Date.valueOf(LocalDate.parse(retrossFlightDto.getEta().substring(0, 10))));
        airlinesAvailabilityItineraryDto.setArrivalTime(
                Time.valueOf(LocalTime.parse(retrossFlightDto.getEta().substring(11, 16))));
        if (retrossFlightDto.getTransit() != null) {
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
