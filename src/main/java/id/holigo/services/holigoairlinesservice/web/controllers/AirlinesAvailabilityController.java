package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class AirlinesAvailabilityController {

    private AirlinesService airlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;


    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityRepository(AirlinesAvailabilityRepository airlinesAvailabilityRepository) {
        this.airlinesAvailabilityRepository = airlinesAvailabilityRepository;
    }

    @Autowired
    public void setAirlinesAvailabilityMapper(AirlinesAvailabilityMapper airlinesAvailabilityMapper) {
        this.airlinesAvailabilityMapper = airlinesAvailabilityMapper;
    }

    @GetMapping("/api/v1/airlines/availabilities")
    public ResponseEntity<ListAvailabilityDto> getAvailabilities(
            @RequestParam("airlinesCode") String airlinesCode,
            @RequestParam("originAirportId") String originAirportId,
            @RequestParam("destinationAirportId") String destinationAirportId,
            @RequestParam("departureDate") String departureDate,
            @RequestParam(name = "returnDate", required = false) String returnDate,
            @RequestParam("tripType") String tripType,
            @RequestParam("adultAmount") int adultAmount,
            @RequestParam("childAmount") int childAmount,
            @RequestParam("infantAmount") int infantAmount,
            @RequestParam("seatClass") String seatClass
    ) {

        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        List<AirlinesAvailability> airlinesAvailabilities = airlinesAvailabilityRepository.getAirlinesAvailability(
                airlinesCode, originAirportId, destinationAirportId, departureDate
        );
        if (airlinesAvailabilities.size() > 0) {
            log.info("GET");
            listAvailabilityDto.setDepartures(airlinesAvailabilities.stream().map(airlinesAvailabilityMapper::airlinesAvailabilityToAirlinesAvailabilityDto).toList());
            return new ResponseEntity<>(listAvailabilityDto, HttpStatus.OK);
        }
        // if not available flights
        RequestScheduleDto requestScheduleDto = RequestScheduleDto.builder()
                .ac(airlinesCode)
                .org(originAirportId)
                .des(destinationAirportId)
                .tgl_dep(departureDate)
                .tgl_ret(returnDate)
                .flight(tripType)
                .adt(adultAmount)
                .chd(childAmount)
                .inf(infantAmount)
                .cabin(seatClass).build();
        log.info("requestAirlinesDto -> {}", requestScheduleDto);
        try {
            listAvailabilityDto = airlinesService.getAvailabilities(requestScheduleDto);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (listAvailabilityDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        airlinesService.saveAvailabilities(listAvailabilityDto);


        return new ResponseEntity<>(listAvailabilityDto, HttpStatus.OK);
    }
}
