package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.repositories.AirlinesCodeRepository;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class AirlinesAvailabilityMapperDecorator
        implements AirlinesAvailabilityMapper {
    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesCodeRepository airlinesCodeRepository;

    @Autowired
    public void setAirlinesAvailabilityMapper(
            AirlinesAvailabilityMapper airlinesAvailabilityMapper) {
        this.airlinesAvailabilityMapper = airlinesAvailabilityMapper;
    }

    @Autowired
    public void setAirlinesCodeRepository(AirlinesCodeRepository airlinesCodeRepository) {
        this.airlinesCodeRepository = airlinesCodeRepository;
    }

    @Override
    public AirlinesAvailabilityDto retrossDepartureDtoDtoToAirlinesAvailabilityDto(
            RetrossDepartureDto retrossDepartureDto) {
        int flightCounter = retrossDepartureDto.getFlights().size();
        int duration = retrossDepartureDto.getFlights().stream()
                .mapToInt(dto -> Integer.parseInt(dto.getDuration())).sum();
        int transit = retrossDepartureDto.getFlights().stream()
                .mapToInt(dto -> Integer.parseInt(dto.getTransit())).sum();
        AirlinesAvailabilityDto airlinesAvailabilityDto = new AirlinesAvailabilityDto();
        airlinesAvailabilityDto.setId(UUID.randomUUID());
        airlinesAvailabilityDto.setAirlinesName("Lion air");
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
                Time.valueOf(LocalTime.parse(retrossDepartureDto.getFlights().get(0).getEta().substring(11, 16))));
        airlinesAvailabilityDto.setDuration(duration);
        airlinesAvailabilityDto.setTransit(transit);
        return airlinesAvailabilityDto;
    }

    @Override
    public ListAvailabilityDto responseScheduleDtoToListAvailabilityDto(
            ResponseScheduleDto responseScheduleDto) {
        List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList = new ArrayList<>();
        responseScheduleDto.getSchedule().getDepartures().forEach(departure -> {
            airlinesAvailabilityDtoList.add(retrossDepartureDtoDtoToAirlinesAvailabilityDto(departure));
        });
        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setDepartures(airlinesAvailabilityDtoList);
        return listAvailabilityDto;
    }
}
