package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesFinalFareRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesFareMapper;
import id.holigo.services.holigoairlinesservice.web.model.FareDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareDto;
import id.holigo.services.holigoairlinesservice.web.model.TripDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Slf4j
@RestController
public class AirlinesFareController {

    private AirlinesFinalFareRepository airlinesFinalFareRepository;
    private AirlinesService airlinesService;

    private AirlinesFareMapper airlinesFareMapper;

    public static final String PATH = "/api/v1/airlines/fares";

    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    @Autowired
    public void setAirlinesFinalFareRepository(AirlinesFinalFareRepository airlinesFinalFareRepository) {
        this.airlinesFinalFareRepository = airlinesFinalFareRepository;
    }

    @Autowired
    public void setAirlinesFareMapper(AirlinesFareMapper airlinesFareMapper) {
        this.airlinesFareMapper = airlinesFareMapper;
    }


    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> createFare(@RequestBody AirlinesFinalFareDto airlinesFinalFareDto, @RequestHeader("user-id") Long userId) {
        log.info("Fare running");
        AirlinesFinalFareDto finalFareForResponse = null;
        for (int i = 0; i < airlinesFinalFareDto.getTrips().size(); i++) {
            TripDto tripDto = airlinesFinalFareDto.getTrips().get(i);
            try {
                finalFareForResponse = airlinesService.createFinalFares(tripDto, userId);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if (finalFareForResponse == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(PATH + "/{id}").buildAndExpand(finalFareForResponse.getId()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }


    @GetMapping(PATH + "/{id}")
    public ResponseEntity<AirlinesFinalFareDto> getFare(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(airlinesFareMapper.airlinesFinalFareToAirlinesFinalFareDto(airlinesFinalFareRepository.getById(id)), HttpStatus.OK);
    }
}
