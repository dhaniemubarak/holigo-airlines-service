package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesCodeRepository;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public abstract class AirlinesAvailabilityMapperDecorator
        implements AirlinesAvailabilityMapper {

    private AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

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

    @Override
    public AirlinesAvailabilityDto retrossDepartureDtoDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto) {
        int flightCounter = retrossDepartureDto.getFlights().size();
        int duration = retrossDepartureDto.getFlights().stream()
                .mapToInt(dto -> Integer.parseInt(dto.getDuration())).sum();
        int transit = retrossDepartureDto.getFlights().size() - 1;
        Map<String, String> airlines = getAirlines(retrossDepartureDto.getFlights().get(0).getFlightNumber().substring(0, 2));
        AirlinesAvailabilityDto airlinesAvailabilityDto = new AirlinesAvailabilityDto();
        airlinesAvailabilityDto.setAirlinesCode(airlines.get("code"));
        airlinesAvailabilityDto.setAirlinesName(airlines.get("name"));
        airlinesAvailabilityDto.setImageUrl(airlines.get("imageUrl"));
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
        return airlinesAvailabilityDto;
    }

    @Override
    public ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto) {
        List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList = new ArrayList<>();
        for (int i = 0; i < responseScheduleDto.getSchedule().getDepartures().size(); i++) {
            RetrossDepartureDto retrossDepartureDto = responseScheduleDto.getSchedule().getDepartures().get(i);
            airlinesAvailabilityDtoList.add(retrossDepartureDtoDtoToAirlinesAvailabilityDto(
                    retrossDepartureDto
            ));
            JsonNode fare = retrossDepartureDto.getFares().get(0).get(0);
            RetrossFareDto fareDto = new RetrossFareDto();
            fareDto.setSubClass(fare.get("SubClass").asText());
            fareDto.setSeatAvb(fare.get("SeatAvb").asInt());
            fareDto.setNta(BigDecimal.valueOf(fare.get("NTA").asDouble()).setScale(2));
            fareDto.setTotalFare(BigDecimal.valueOf(fare.get("TotalFare").asDouble()).setScale(2));
            fareDto.setSelectedIdDep(fare.get("selectedIDdep").asText());
            AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = airlinesAvailabilityFareMapper.retrossFareToAirlinesAvailabilityFareDto(fareDto);
            airlinesAvailabilityDtoList.get(i).setFare(airlinesAvailabilityFareDto);

        }
        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setDepartures(airlinesAvailabilityDtoList);
        return listAvailabilityDto;
    }

    @Override
    public AirlinesAvailability airlinesAvailabilityDtoToAirlinesAvailability(AirlinesAvailabilityDto airlinesAvailabilityDto) {
        AirlinesAvailability airlinesAvailability = airlinesAvailabilityMapper.airlinesAvailabilityDtoToAirlinesAvailability(airlinesAvailabilityDto);
        try {
            airlinesAvailability.setFare(objectMapper.writeValueAsString(airlinesAvailabilityDto.getFare()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return airlinesAvailability;
    }

    @Override
    public AirlinesAvailabilityDto airlinesAvailabilityToAirlinesAvailabilityDto(AirlinesAvailability airlinesAvailability) {
        AirlinesAvailabilityDto airlinesAvailabilityDto = airlinesAvailabilityMapper.airlinesAvailabilityToAirlinesAvailabilityDto(airlinesAvailability);
        try {
            airlinesAvailabilityDto.setFare(airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(
                    objectMapper.readValue(airlinesAvailability.getFare(), AirlinesAvailabilityFareDto.class)
            ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return airlinesAvailabilityDto;
    }

    public Map<String, String> getAirlines(String airlinesCode) {
        Map<String, String> airlines = new HashMap<>();
        airlines.put("code", airlinesCode);
        switch (airlinesCode) {
            case "JT":
                airlines.put("code", "JT");
                airlines.put("name", "Lion Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JT.png");
                break;
            case "3K":
                airlines.put("name", "JetStar");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/3K.png");
                break;
            case "AA":
                airlines.put("name", "American Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/AA.png");
                break;
            case "AK":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/AK.png");
                break;
            case "BA":
                airlines.put("name", "British Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/BA.png");
                break;
            case "CI":
                airlines.put("name", "China Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CI.png");
                break;
            case "CX":
                airlines.put("name", "Cathay Pacific");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CX.png");
                break;
            case "CZ":
                airlines.put("name", "China Southen");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/CZ.png");
                break;
            case "D7":
                airlines.put("name", "American Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/D7.png");
                break;
            case "EK":
                airlines.put("name", "Emirates");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/EK.png");
                break;
            case "EY":
                airlines.put("name", "Etihad Airways");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/EY.png");
                break;
            case "FD":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/FD.png");
                break;
            case "GA":
                airlines.put("name", "Garuda Indonesia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/GA.png");
                break;
            case "I5":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/I5.png");
                break;
            case "IA":
                airlines.put("name", "International Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IA.png");
                break;
            case "IL":
                airlines.put("name", "Trigana Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IL.png");
                break;
            case "IN":
                airlines.put("name", "NAM Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IN.png");
                break;
            case "IU":
            case "IW":
                airlines.put("code", "JT");
                airlines.put("name", "Whings Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/IU.png");
                break;
            case "JL":
                airlines.put("name", "Japan Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JL.png");
                break;
            case "JQ":
                airlines.put("name", "Jetstar");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/JQ.png");
                break;
            case "KD":
                airlines.put("name", "Kalstar Aviation");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KD.png");
                break;
            case "KE":
                airlines.put("name", "Korean Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KE.png");
                break;
            case "KL":
                airlines.put("name", "KLM");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/KL.png");
                break;
            case "MH":
                airlines.put("name", "Malaysia Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MH.png");
                break;
            case "MI":
                airlines.put("name", "SILKAIR");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MI.png");
                break;
            case "MV":
                airlines.put("name", "Batik Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/MV.png");
                break;
            case "NH":
                airlines.put("name", "ANA");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/NH.png");
                break;
            case "ID":
                airlines.put("code", "JT");
                airlines.put("name", "Batik Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/ID.png");
                break;
            case "OD":
                airlines.put("name", "Malindo Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/OD.png");
                break;
            case "QF":
                airlines.put("name", "Qantas");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QF.png");
                break;
            case "QG":
                airlines.put("name", "Citilink");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QG.png");
                break;
            case "QR":
                airlines.put("name", "Qatar Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QR.png");
                break;
            case "QZ":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/QZ.png");
                break;
            case "SJ":
                airlines.put("name", "Sriwijaya Air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SJ.png");
                break;
            case "SL":
                airlines.put("name", "Thai Lion air");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SL.png");
                break;
            case "SQ":
                airlines.put("name", "Singapore Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SQ.png");
                break;
            case "SV":
                airlines.put("name", "Saudia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/SV.png");
                break;
            case "TG":
                airlines.put("name", "Thai");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TG.png");
                break;
            case "TK":
                airlines.put("name", "Turkish Airlines");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TK.png");
                break;
            case "TL":
                airlines.put("name", "Airnorth");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TL.png");
                break;
            case "TR":
                airlines.put("name", "tigerair");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TR.png");
                break;
            case "TZ":
                airlines.put("name", "scoot");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/TZ.png");
                break;
            case "VY":
                airlines.put("name", "vueling");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/VY.png");
                break;
            case "XJ":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/XJ.png");
                break;
            case "XT":
                airlines.put("name", "AirAsia");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/XT.png");
                break;
            case "Z2":
                airlines.put("name", "ZestAir");
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/Z2.png");
                break;
            default:
                airlines.put("name", airlinesCode);
                airlines.put("imageUrl", "https://www.gstatic.com/flights/airline_logos/70px/8B.png");
                break;
        }
        return airlines;
    }
}
