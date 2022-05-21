package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityItineraryDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFlightDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public abstract class AirlinesAvailabilityItineraryMapperDecorator implements AirlinesAvailabilityItineraryMapper {

    private Airlines airlines;

    private AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper;

    @Autowired
    public void setAirlinesAvailabilityItineraryMapper(AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper) {
        this.airlinesAvailabilityItineraryMapper = airlinesAvailabilityItineraryMapper;
    }

    @Autowired
    public void setAirlines(Airlines airlines) {
        this.airlines = airlines;
    }

    @Override
    public AirlinesAvailabilityItineraryDto retrossFlightDtoToAirlinesAvailabilityItineraryDto(RetrossFlightDto retrossFlightDto) {
        Map<String, String> airlinesMap = airlines.getAirlines(retrossFlightDto.getFlightNumber().substring(0, 2));
        AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto = airlinesAvailabilityItineraryMapper.retrossFlightDtoToAirlinesAvailabilityItineraryDto(retrossFlightDto);
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

        return airlinesAvailabilityItineraryDto;
    }

    @Override
    public AirlinesAvailabilityItinerary airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto) {
        return airlinesAvailabilityItineraryMapper.airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(airlinesAvailabilityItineraryDto);
    }
}
