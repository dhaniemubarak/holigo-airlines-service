package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.exceptions.NotFoundException;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public abstract class AirlinesAvailabilityMapperDecorator
        implements AirlinesAvailabilityMapper {

    private Airlines airlines;
    private AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper;

    private AirlinesAvailabilityPriceMapper airlinesAvailabilityPriceMapper;

    private AirportRepository airportRepository;

    private AirportMapper airportMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public void setAirlinesAvailabilityFareMapper(AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper) {
        this.airlinesAvailabilityFareMapper = airlinesAvailabilityFareMapper;
    }

    @Autowired
    public void setAirlinesAvailabilityMapper(AirlinesAvailabilityMapper airlinesAvailabilityMapper) {
        this.airlinesAvailabilityMapper = airlinesAvailabilityMapper;
    }

    @Autowired
    public void setAirportMapper(AirportMapper airportMapper) {
        this.airportMapper = airportMapper;
    }

    @Autowired
    public void setAirportRepository(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Autowired
    public void setAirlines(Airlines airlines) {
        this.airlines = airlines;
    }

    @Autowired
    public void setAirlinesAvailabilityItineraryMapper(AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper) {
        this.airlinesAvailabilityItineraryMapper = airlinesAvailabilityItineraryMapper;
    }

    @Autowired
    public void setAirlinesAvailabilityPriceMapper(AirlinesAvailabilityPriceMapper airlinesAvailabilityPriceMapper) {
        this.airlinesAvailabilityPriceMapper = airlinesAvailabilityPriceMapper;
    }

    @Override
    public AirlinesAvailabilityDto retrossDepartureDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto, InquiryDto inquiryDto) {
        int flightCounter = retrossDepartureDto.getFlights().size();
        int duration = retrossDepartureDto.getFlights().stream()
                .mapToInt(dto -> Integer.parseInt(dto.getDuration())).sum();
        int transit = retrossDepartureDto.getFlights().size() - 1;
        Map<String, String> airlinesMap = airlines.getAirlines(retrossDepartureDto.getFlights().get(0).getFlightNumber().substring(0, 2));
        AirlinesAvailabilityDto airlinesAvailabilityDto = new AirlinesAvailabilityDto();
        airlinesAvailabilityDto.setId(UUID.randomUUID());
        airlinesAvailabilityDto.setAirlinesCode(airlinesMap.get("code"));
        airlinesAvailabilityDto.setAirlinesName(airlinesMap.get("name"));
        airlinesAvailabilityDto.setImageUrl(airlinesMap.get("imageUrl"));
        airlinesAvailabilityDto.setFlightNumber(retrossDepartureDto.getFlights().get(0).getFlightNumber());
        airlinesAvailabilityDto.setDepartureDate(
                Date.valueOf(LocalDate.parse(retrossDepartureDto.getFlights().get(0).getEtd().substring(0, 10))));
        airlinesAvailabilityDto.setDepartureTime(
                Time.valueOf(LocalTime.parse(retrossDepartureDto.getFlights().get(0).getEtd().substring(11, 16))));
        airlinesAvailabilityDto.setArrivalDate(Date.valueOf(LocalDate.parse(retrossDepartureDto.getFlights()
                .get(flightCounter - 1).getEta().substring(0, 10))));
        airlinesAvailabilityDto.setArrivalTime(
                Time.valueOf(LocalTime.parse(retrossDepartureDto.getFlights().get(flightCounter - 1).getEta().substring(11, 16))));
        airlinesAvailabilityDto.setDuration(duration);
        airlinesAvailabilityDto.setTransit(transit);
        if (Objects.equals(retrossDepartureDto.getFlights().get(0).getStd(), inquiryDto.getOriginAirport().getId())) {
            airlinesAvailabilityDto.setOriginAirport(inquiryDto.getOriginAirport());
            airlinesAvailabilityDto.setDestinationAirport(inquiryDto.getDestinationAirport());
        } else {
            airlinesAvailabilityDto.setOriginAirport(inquiryDto.getDestinationAirport());
            airlinesAvailabilityDto.setDestinationAirport(inquiryDto.getOriginAirport());
        }


        List<AirlinesAvailabilityFareDto> fares = new ArrayList<>();
        for (JsonNode fare : retrossDepartureDto.getFares()) {

            fare.forEach(value -> {
                try {
                    AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper
                            .retrossFareToAirlinesAvailabilityFareDto(objectMapper.readValue(value.toString(), RetrossFareDto.class));
                    if (airlinesAvailabilityDto.getFare() == null) {
                        // Check for promo, economy, business, and first class
                        airlinesAvailabilityDto.setFare(
                                airlinesAvailabilityPriceMapper
                                        .airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, inquiryDto.getUserId())
                        );
                    }
                    fares.add(airlinesAvailabilityFareDto);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                log.info("value -> {}", value);

            });
        }

        airlinesAvailabilityDto.setFares(fares);
        List<AirlinesAvailabilityItineraryDto> airlinesAvailabilityItineraryDtoList = new ArrayList<>();
        for (int i = 0; i < retrossDepartureDto.getFlights().size(); i++) {
            AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto = airlinesAvailabilityItineraryMapper.retrossFlightDtoToAirlinesAvailabilityItineraryDto(retrossDepartureDto.getFlights().get(i), inquiryDto);
            airlinesAvailabilityItineraryDto.setLeg(i + 1);
            airlinesAvailabilityItineraryDtoList.add(airlinesAvailabilityItineraryDto);
        }
        airlinesAvailabilityDto.setItineraries(airlinesAvailabilityItineraryDtoList);
        return airlinesAvailabilityDto;
    }

    @Override
    public ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto, InquiryDto inquiryDto) {
        List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList = new ArrayList<>();
        for (int i = 0; i < responseScheduleDto.getSchedule().getDepartures().size(); i++) {
            RetrossDepartureDto retrossDepartureDto = responseScheduleDto.getSchedule().getDepartures().get(i);
            airlinesAvailabilityDtoList.add(retrossDepartureDtoToAirlinesAvailabilityDto(
                    retrossDepartureDto, inquiryDto
            ));
        }
        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setDepartures(airlinesAvailabilityDtoList);
        if (responseScheduleDto.getSchedule().getReturns() != null) {
            List<AirlinesAvailabilityDto> airlinesAvailabilityReturnsDtoList = new ArrayList<>();
            for (int i = 0; i < responseScheduleDto.getSchedule().getReturns().size(); i++) {
                RetrossDepartureDto retrossDepartureDto = responseScheduleDto.getSchedule().getReturns().get(i);
                airlinesAvailabilityReturnsDtoList.add(retrossDepartureDtoToAirlinesAvailabilityDto(
                        retrossDepartureDto, inquiryDto
                ));
            }
            listAvailabilityDto.setReturns(airlinesAvailabilityReturnsDtoList);
        }
        return listAvailabilityDto;
    }

    @Override
    public AirlinesAvailability airlinesAvailabilityDtoToAirlinesAvailability(AirlinesAvailabilityDto airlinesAvailabilityDto) {
        AirlinesAvailability airlinesAvailability = airlinesAvailabilityMapper.airlinesAvailabilityDtoToAirlinesAvailability(airlinesAvailabilityDto);
        try {
            airlinesAvailabilityDto.getFares().forEach(airlinesAvailabilityFareDto ->
                    airlinesAvailability.addToFares(
                            airlinesAvailabilityFareMapper
                                    .airlinesAvailabilityFareDtoToAirlinesAvailabilityFare(airlinesAvailabilityFareDto)
                    )
            );
            airlinesAvailabilityDto.getItineraries().forEach(itineraryDto ->
                    airlinesAvailability.addToItineraries(
                            airlinesAvailabilityItineraryMapper
                                    .airlinesAvailabilityItineraryDtoToAirlinesAvailabilityItinerary(itineraryDto)
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return airlinesAvailability;
    }

    @Override
    public AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability, Long userId, Integer passengerAmount) {
        AirlinesAvailabilityDto airlinesAvailabilityDto = this.airlinesAvailabilityMapper.airlinesAvailabilityToAirlinesAvailabilityDto(airlinesAvailability, userId, passengerAmount);
        airlinesAvailabilityDto.setOriginAirport(airportMapper.airportToAirportDto(airportRepository.getById(airlinesAvailability.getOriginAirportId())));
        airlinesAvailabilityDto.setDestinationAirport(airportMapper.airportToAirportDto(airportRepository.getById(airlinesAvailability.getDestinationAirportId())));
        AirlinesAvailabilityFare airlinesAvailabilityFare = airlinesAvailability.getFares().stream().filter(fare -> fare.getSeatAvailable() >= passengerAmount).findFirst().orElseThrow();
        AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(airlinesAvailabilityFare);
        airlinesAvailabilityDto.setFare(airlinesAvailabilityPriceMapper.airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, userId));
        return airlinesAvailabilityDto;
    }


}
