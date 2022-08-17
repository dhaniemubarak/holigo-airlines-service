package id.holigo.services.holigoairlinesservice.web.controllers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesFinalFareRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesFinalFareMapper;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestFinalFareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class AirlinesFareController {

    private AirlinesFinalFareRepository airlinesFinalFareRepository;
    private AirlinesService airlinesService;

    private AirlinesFinalFareMapper airlinesFinalFareMapper;

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
    public void setAirlinesFareMapper(AirlinesFinalFareMapper airlinesFinalFareMapper) {
        this.airlinesFinalFareMapper = airlinesFinalFareMapper;
    }


    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> createFare(@RequestBody RequestFinalFareDto requestFinalFareDto, @RequestHeader("user-id") Long userId) {
        boolean isInternational = requestFinalFareDto.getTrips().stream().anyMatch(tripDto -> tripDto.getInquiry().getAirlinesCode().equals("IA"));
        AirlinesFinalFare airlinesFinalFare;
        if (isInternational) {
            airlinesFinalFare = airlinesService.createInternationalFinalFare(requestFinalFareDto, userId);
        } else {
            airlinesFinalFare = airlinesService.createFinalFares(requestFinalFareDto, userId);
        }

        if (airlinesFinalFare == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(PATH + "/{id}").buildAndExpand(airlinesFinalFare.getId()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }


    @GetMapping(PATH + "/{id}")
    public ResponseEntity<AirlinesFinalFareDto> getFare(@PathVariable("id") UUID id) {
        Optional<AirlinesFinalFare> fetchAirlinesFinalFare = airlinesFinalFareRepository.findById(id);
        if (fetchAirlinesFinalFare.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(airlinesFinalFareMapper.airlinesFinalFareToAirlinesFinalFareDto(fetchAirlinesFinalFare.get()), HttpStatus.OK);
    }
}
