package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.web.exceptions.AvailabilitiesException;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        int fareCounter = retrossDepartureDto.getFares().size();
        int duration;
        int transit;
        duration = retrossDepartureDto.getFlights().stream()
                .mapToInt(dto -> Integer.parseInt(dto.getDuration())).sum();
        transit = retrossDepartureDto.getFlights().size() - 1;
        Map<String, String> airlinesMap = airlines.getAirlines(retrossDepartureDto.getFlights().get(0).getFlightNumber().substring(0, 2));
        AirlinesAvailabilityDto airlinesAvailabilityDto = new AirlinesAvailabilityDto();
        airlinesAvailabilityDto.setId(UUID.randomUUID());
        airlinesAvailabilityDto.setAirlinesCode(airlinesMap.get("code"));
        airlinesAvailabilityDto.setAirlinesName(airlinesMap.get("name"));
        airlinesAvailabilityDto.setImageUrl(airlinesMap.get("imageUrl"));
        airlinesAvailabilityDto.setFlightNumber(retrossDepartureDto.getFlights().get(0).getFlightNumber());
        Timestamp etd = Timestamp.valueOf(retrossDepartureDto.getFlights().get(0).getEtd() + ":00");
        Timestamp eta = Timestamp.valueOf(retrossDepartureDto.getFlights().get(flightCounter - 1).getEta() + ":00");
        airlinesAvailabilityDto.setDepartureDate(new Date(etd.getTime()));
        airlinesAvailabilityDto.setDepartureTime(new Time(etd.getTime()));
        airlinesAvailabilityDto.setArrivalDate(new Date(eta.getTime()));
        airlinesAvailabilityDto.setArrivalTime(new Time(eta.getTime()));
        airlinesAvailabilityDto.setDuration(duration);
        airlinesAvailabilityDto.setTransit(transit);
        airlinesAvailabilityDto.setSeatClass(inquiryDto.getSeatClass());
        if (Objects.equals(retrossDepartureDto.getFlights().get(0).getStd(), inquiryDto.getOriginAirport().getId())) {
            airlinesAvailabilityDto.setOriginAirport(inquiryDto.getOriginAirport());
            airlinesAvailabilityDto.setDestinationAirport(inquiryDto.getDestinationAirport());
        } else {
            airlinesAvailabilityDto.setOriginAirport(inquiryDto.getDestinationAirport());
            airlinesAvailabilityDto.setDestinationAirport(inquiryDto.getOriginAirport());
        }
        List<AirlinesAvailabilityFareDto> fares = new ArrayList<>();
        List<String> selectedId = new ArrayList<>();
        List<String> selectedSubclass = new ArrayList<>();
        AirlinesAvailabilityPriceDto airlinesAvailabilityPriceDto = AirlinesAvailabilityPriceDto.builder()
                .fareAmount(BigDecimal.ZERO.setScale(2, RoundingMode.UP))
                .hpAmount(BigDecimal.ZERO.setScale(2, RoundingMode.UP))
                .hpcAmount(BigDecimal.ZERO.setScale(2, RoundingMode.UP))
                .normalFare(BigDecimal.ZERO.setScale(2, RoundingMode.UP))
                .fareAmount(BigDecimal.ZERO.setScale(2, RoundingMode.UP))
                .seatAvailable(0)
                .build();
        List<AirlinesAvailabilityItineraryDto> airlinesAvailabilityItineraryDtoList = new ArrayList<>();
        AtomicInteger indexFlight = new AtomicInteger();
        for (RetrossFlightDto retrossFlightDto : retrossDepartureDto.getFlights()) {
            AirlinesAvailabilityItineraryDto airlinesAvailabilityItineraryDto = airlinesAvailabilityItineraryMapper.retrossFlightDtoToAirlinesAvailabilityItineraryDto(retrossFlightDto, inquiryDto);
            airlinesAvailabilityItineraryDto.setLeg(indexFlight.get() + 1);
            if (inquiryDto.getAirlinesCode().equals("IA")) {
                airlinesAvailabilityDto.setIsInternational(true);
                if (flightCounter != fareCounter && indexFlight.get() != 0) {
                    airlinesAvailabilityItineraryDto.setNormalFare(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setFareAmount(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setNtaAmount(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setNraAmount(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setAdultRates(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setChildRates(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setInfantRates(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setBasicRates(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setIsPriceInclude(true);
                    airlinesAvailabilityItineraryDtoList.add(airlinesAvailabilityItineraryDto);
                    indexFlight.getAndIncrement();
                    continue;
                }
                retrossDepartureDto.getFares().forEach(value -> {
                    AirlinesAvailabilityFareDto airlinesAvailabilityFareDto;
                    try {
                        airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper
                                .retrossInternationalFareToAirlinesAvailabilityFareDto(objectMapper.readValue(value.toString(), RetrossInternationalFareDto.class));
                        airlinesAvailabilityFareDto.setLeg(airlinesAvailabilityItineraryDto.getLeg());
                        AirlinesAvailabilityPriceDto temp = airlinesAvailabilityPriceMapper.airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, inquiryDto.getUserId());
                        selectedId.add(temp.getSelectedId());
                        selectedSubclass.add(temp.getSubclass());
                        airlinesAvailabilityPriceDto.setSeatAvailable(temp.getSeatAvailable());
                        airlinesAvailabilityItineraryDto.setNtaAmount(airlinesAvailabilityFareDto.getNtaAmount());
                        airlinesAvailabilityItineraryDto.setNraAmount(airlinesAvailabilityFareDto.getNraAmount());
                        airlinesAvailabilityItineraryDto.setSubclass(airlinesAvailabilityFareDto.getSubclass());
                        airlinesAvailabilityItineraryDto.setSeatAvailable(airlinesAvailabilityFareDto.getSeatAvailable());
                        airlinesAvailabilityItineraryDto.setSelectedId(airlinesAvailabilityFareDto.getSelectedId());
                        airlinesAvailabilityItineraryDto.setAdultRates(airlinesAvailabilityFareDto.getAdultRates());
                        airlinesAvailabilityItineraryDto.setChildRates(airlinesAvailabilityFareDto.getChildRates());
                        airlinesAvailabilityItineraryDto.setInfantRates(airlinesAvailabilityFareDto.getInfantRates());
                        airlinesAvailabilityItineraryDto.setBasicRates(airlinesAvailabilityFareDto.getBasicRates());
                        airlinesAvailabilityItineraryDto.setIsPriceInclude(false);
                        airlinesAvailabilityItineraryDto.setNormalFare(temp.getNormalFare());
                        airlinesAvailabilityItineraryDto.setFareAmount(temp.getFareAmount());
                        airlinesAvailabilityItineraryDto.setHpAmount(temp.getHpAmount());
                        airlinesAvailabilityItineraryDto.setHpcAmount(temp.getHpcAmount());
                        airlinesAvailabilityPriceDto.setFareAmount(airlinesAvailabilityPriceDto.getFareAmount().add(temp.getFareAmount()).setScale(2, RoundingMode.UP));
                        airlinesAvailabilityPriceDto.setHpAmount(airlinesAvailabilityPriceDto.getHpAmount().add(temp.getHpAmount()).setScale(2, RoundingMode.UP));
                        airlinesAvailabilityPriceDto.setHpcAmount(airlinesAvailabilityPriceDto.getHpcAmount().add(temp.getHpcAmount()).setScale(2, RoundingMode.UP));
                        airlinesAvailabilityPriceDto.setNormalFare(airlinesAvailabilityPriceDto.getNormalFare().add(temp.getNormalFare()).setScale(2, RoundingMode.UP));
                        fares.add(airlinesAvailabilityFareDto);
                    } catch (JsonProcessingException e) {
                        throw new AvailabilitiesException(e);
                    }
                    if (airlinesAvailabilityDto.getFare() == null) {
                        airlinesAvailabilityDto.setFare(
                                airlinesAvailabilityPriceMapper
                                        .airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, inquiryDto.getUserId())
                        );
                    }

                });
                airlinesAvailabilityDto.setFares(fares);
            } else {
                airlinesAvailabilityDto.setIsInternational(false);
                if (flightCounter != fareCounter && indexFlight.get() != 0) {
                    airlinesAvailabilityItineraryDto.setNormalFare(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setFareAmount(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setNtaAmount(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setNraAmount(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setAdultRates(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setChildRates(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setInfantRates(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setBasicRates(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.UP));
                    airlinesAvailabilityItineraryDto.setIsPriceInclude(true);
                    airlinesAvailabilityItineraryDtoList.add(airlinesAvailabilityItineraryDto);
                    indexFlight.getAndIncrement();
                    continue;
                }
                AtomicInteger fareIndex = new AtomicInteger();
                JsonNode faresNode = retrossDepartureDto.getFares().get(indexFlight.get());
                faresNode.forEach(value -> {
                    String seatClass;
                    try {
                        AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper
                                .retrossFareToAirlinesAvailabilityFareDto(objectMapper.readValue(value.toString(), RetrossFareDto.class));
                        seatClass = subclassToSeatClass(airlinesAvailabilityDto.getFlightNumber().substring(0, 2), airlinesAvailabilityFareDto.getSubclass());
                        if (inquiryDto.getSeatClass().equals(seatClass)) {
                            airlinesAvailabilityFareDto.setLeg(airlinesAvailabilityItineraryDto.getLeg());
                            // insert
                            if (fareIndex.get() == 0) {
                                AirlinesAvailabilityPriceDto temp = airlinesAvailabilityPriceMapper.airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, inquiryDto.getUserId());
                                selectedId.add(temp.getSelectedId());
                                selectedSubclass.add(temp.getSubclass());
                                if (indexFlight.get() == 0) {
                                    airlinesAvailabilityPriceDto.setSeatAvailable(temp.getSeatAvailable());
                                }
                                airlinesAvailabilityItineraryDto.setNtaAmount(airlinesAvailabilityFareDto.getNtaAmount());
                                airlinesAvailabilityItineraryDto.setNraAmount(airlinesAvailabilityFareDto.getNraAmount());
                                airlinesAvailabilityItineraryDto.setSubclass(airlinesAvailabilityFareDto.getSubclass());
                                airlinesAvailabilityItineraryDto.setSeatAvailable(airlinesAvailabilityFareDto.getSeatAvailable());
                                airlinesAvailabilityItineraryDto.setSelectedId(airlinesAvailabilityFareDto.getSelectedId());
                                airlinesAvailabilityItineraryDto.setAdultRates(airlinesAvailabilityFareDto.getAdultRates());
                                airlinesAvailabilityItineraryDto.setChildRates(airlinesAvailabilityFareDto.getChildRates());
                                airlinesAvailabilityItineraryDto.setInfantRates(airlinesAvailabilityFareDto.getInfantRates());
                                airlinesAvailabilityItineraryDto.setBasicRates(airlinesAvailabilityFareDto.getBasicRates());
                                airlinesAvailabilityItineraryDto.setIsPriceInclude(false);
                                airlinesAvailabilityItineraryDto.setNormalFare(temp.getNormalFare());
                                airlinesAvailabilityItineraryDto.setFareAmount(temp.getFareAmount());
                                airlinesAvailabilityItineraryDto.setHpAmount(temp.getHpAmount());
                                airlinesAvailabilityItineraryDto.setHpcAmount(temp.getHpcAmount());
                                airlinesAvailabilityPriceDto.setFareAmount(airlinesAvailabilityPriceDto.getFareAmount().add(temp.getFareAmount()).setScale(2, RoundingMode.UP));
                                airlinesAvailabilityPriceDto.setHpAmount(airlinesAvailabilityPriceDto.getHpAmount().add(temp.getHpAmount()).setScale(2, RoundingMode.UP));
                                airlinesAvailabilityPriceDto.setHpcAmount(airlinesAvailabilityPriceDto.getHpcAmount().add(temp.getHpcAmount()).setScale(2, RoundingMode.UP));
                                airlinesAvailabilityPriceDto.setNormalFare(airlinesAvailabilityPriceDto.getNormalFare().add(temp.getNormalFare()).setScale(2, RoundingMode.UP));
                            }
                            fares.add(airlinesAvailabilityFareDto);
                        }
                    } catch (JsonProcessingException e) {
                        throw new AvailabilitiesException(e);
                    }
                    fareIndex.getAndIncrement();
                });
                airlinesAvailabilityDto.setFares(fares);
            }
            airlinesAvailabilityItineraryDtoList.add(airlinesAvailabilityItineraryDto);
            indexFlight.getAndIncrement();
        }
        airlinesAvailabilityPriceDto.setSelectedId(String.join(",", selectedId));
        airlinesAvailabilityPriceDto.setSubclass(String.join(",", selectedSubclass));
        airlinesAvailabilityDto.setItineraries(airlinesAvailabilityItineraryDtoList);
        airlinesAvailabilityDto.setFare(airlinesAvailabilityPriceDto);
        return airlinesAvailabilityDto;
    }

    @Override
    public ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto, InquiryDto inquiryDto) {
        List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList = new ArrayList<>();
        AtomicInteger indexDeparture = new AtomicInteger();
        for (RetrossDepartureDto retrossDepartureDto : responseScheduleDto.getSchedule().getDepartures()) {
            AirlinesAvailabilityDto airlinesAvailabilityDto = retrossDepartureDtoToAirlinesAvailabilityDto(retrossDepartureDto, inquiryDto);
            if (airlinesAvailabilityDto.getFare() != null || airlinesAvailabilityDto.getFare().getFareAmount().compareTo(BigDecimal.ZERO) != 0) {
                airlinesAvailabilityDtoList.add(airlinesAvailabilityDto);
            }
            indexDeparture.getAndIncrement();
        }
        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setDepartures(airlinesAvailabilityDtoList);
        if (responseScheduleDto.getSchedule().getReturns() != null) {
            List<AirlinesAvailabilityDto> airlinesAvailabilityReturnsDtoList = new ArrayList<>();
            for (RetrossDepartureDto retrossDepartureDto : responseScheduleDto.getSchedule().getReturns()) {
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
        airlinesAvailability.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
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
//        if (!airlinesAvailability.getAirlinesCode().equals("IA")) {
        List<String> selectedId = new ArrayList<>();
        List<String> subclasses = new ArrayList<>();
        AirlinesAvailabilityPriceDto airlinesAvailabilityFareDto = AirlinesAvailabilityPriceDto.builder()
                .hpcAmount(BigDecimal.ZERO)
                .hpAmount(BigDecimal.ZERO)
                .normalFare(BigDecimal.ZERO)
                .fareAmount(BigDecimal.ZERO)
                .build();
        airlinesAvailabilityDto.setItineraries(airlinesAvailability.getItineraries().stream().map(airlinesAvailabilityItinerary -> airlinesAvailabilityItineraryMapper.airlinesAvailabilityItineraryToAirlinesAvailabilityItineraryDto(airlinesAvailabilityItinerary, userId)).collect(Collectors.toList()));
        airlinesAvailabilityDto.getItineraries().forEach(airlinesAvailabilityItineraryDto -> {
            selectedId.add(airlinesAvailabilityItineraryDto.getSelectedId());
            subclasses.add(airlinesAvailabilityDto.getSeatClass());
            airlinesAvailabilityFareDto.setSeatAvailable(airlinesAvailabilityItineraryDto.getSeatAvailable());
            airlinesAvailabilityFareDto.setFareAmount(airlinesAvailabilityFareDto.getFareAmount().add(airlinesAvailabilityItineraryDto.getFareAmount()).setScale(2, RoundingMode.UP));
            airlinesAvailabilityFareDto.setHpcAmount(airlinesAvailabilityFareDto.getHpcAmount().add(airlinesAvailabilityItineraryDto.getHpcAmount()).setScale(2, RoundingMode.UP));
            airlinesAvailabilityFareDto.setHpAmount(airlinesAvailabilityFareDto.getHpAmount().add(airlinesAvailabilityItineraryDto.getHpAmount()).setScale(2, RoundingMode.UP));
            airlinesAvailabilityFareDto.setNormalFare(airlinesAvailabilityFareDto.getNormalFare().add(airlinesAvailabilityItineraryDto.getNormalFare()).setScale(2, RoundingMode.UP));

        });

//            AirlinesAvailabilityFare airlinesAvailabilityFare = airlinesAvailability.getFares().stream().filter(fare -> fare.getSeatAvailable() >= passengerAmount).findFirst().orElseThrow();
//            AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(airlinesAvailabilityFare);
        airlinesAvailabilityFareDto.setSelectedId(String.join(",", selectedId));
        airlinesAvailabilityFareDto.setSubclass(String.join(",", subclasses));
        airlinesAvailabilityDto.setFare(airlinesAvailabilityFareDto);
//        }
        return airlinesAvailabilityDto;
    }

    private String subclassToSeatClass(String airlinesCode, String subclass) {
        String seatClass = "E";
        switch (airlinesCode) {
            case "SJ", "IN" -> {
                switch (subclass) {
                    case "C", "D", "I" -> seatClass = "B";
                }
            }
            case "GA" -> {
                switch (subclass) {
                    case "F", "A" -> seatClass = "F";
                    case "C", "J", "D", "I" -> seatClass = "B";
                }
            }
            case "JT", "ID", "IW" -> {
                switch (subclass) {
                    case "C", "J", "D", "I", "Z" -> seatClass = "B";
                }
            }
        }
        return seatClass;
    }
}
