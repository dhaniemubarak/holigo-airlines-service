package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class AirlinesAvailabilityMapperDecorator
        implements AirlinesAvailabilityMapper {

    private Airlines airlines;
    private AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesAvailabilityItineraryMapper airlinesAvailabilityItineraryMapper;

    private AirlinesAvailabilityPriceMapper airlinesAvailabilityPriceMapper;

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
    public AirlinesAvailabilityDto retrossDepartureDtoDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto, Long userId) {
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
        airlinesAvailabilityDto.setOriginAirportId(retrossDepartureDto.getFlights().get(0).getStd());
        airlinesAvailabilityDto.setDestinationAirportId(retrossDepartureDto.getFlights().get(flightCounter - 1).getSta());
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
                                        .airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, userId)
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
        airlinesAvailabilityDto.setItineraries(retrossDepartureDto.getFlights().stream().map(airlinesAvailabilityItineraryMapper::retrossFlightDtoToAirlinesAvailabilityItineraryDto).collect(Collectors.toList()));
        return airlinesAvailabilityDto;
    }

    @Override
    public ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto, Long userId) {
        List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList = new ArrayList<>();
        for (int i = 0; i < responseScheduleDto.getSchedule().getDepartures().size(); i++) {
            RetrossDepartureDto retrossDepartureDto = responseScheduleDto.getSchedule().getDepartures().get(i);
            airlinesAvailabilityDtoList.add(retrossDepartureDtoDtoToAirlinesAvailabilityDto(
                    retrossDepartureDto, userId
            ));
//            JsonNode fare = retrossDepartureDto.getFares().get(0).get(0);
//            RetrossFareDto fareDto = new RetrossFareDto();
//            fareDto.setSubClass(fare.get("SubClass").asText());
//            fareDto.setSeatAvb(fare.get("SeatAvb").asInt());
//            fareDto.setNta(BigDecimal.valueOf(fare.get("NTA").asDouble()).setScale(2, RoundingMode.UP));
//            fareDto.setTotalFare(BigDecimal.valueOf(fare.get("TotalFare").asDouble()).setScale(2, RoundingMode.UP));
//            fareDto.setSelectedIdDep(fare.get("selectedIDdep").asText());
//            AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper.retrossFareToAirlinesAvailabilityFareDto(fareDto);
//            airlinesAvailabilityDtoList.get(i).setFare(airlinesAvailabilityFareDto);

        }
        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setDepartures(airlinesAvailabilityDtoList);
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
    public AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability) {
        return this.airlinesAvailabilityMapper.airlinesAvailabilityToAirlinesAvailabilityDto(airlinesAvailability);
    }


}
