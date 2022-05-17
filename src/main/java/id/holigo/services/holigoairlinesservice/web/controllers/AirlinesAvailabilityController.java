package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.repositories.InquiryRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.InquiryMapper;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class AirlinesAvailabilityController {

    private AirlinesService airlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private InquiryRepository inquiryRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private InquiryMapper inquiryMapper;


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

    @Autowired
    public void setInquiryRepository(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Autowired
    public void setInquiryMapper(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }

    @GetMapping("/api/v1/airlines/availabilities")
    public ResponseEntity<ListAvailabilityDto> getAvailabilities(
            @RequestParam("airlinesCode") String airlinesCode,
            @RequestParam("originAirportId") String originAirportId,
            @RequestParam("destinationAirportId") String destinationAirportId,
            @RequestParam("departureDate") Date departureDate,
            @RequestParam(name = "returnDate", required = false) Date returnDate,
            @RequestParam("tripType") String tripType,
            @RequestParam("adultAmount") int adultAmount,
            @RequestParam("childAmount") int childAmount,
            @RequestParam("infantAmount") int infantAmount,
            @RequestParam("seatClass") String seatClass
    ) {
        log.info(departureDate.toString());
        Inquiry inquiry;
        Optional<Inquiry> fetchInquiry = inquiryRepository.getInquiry(airlinesCode, originAirportId, destinationAirportId,
                departureDate.toString(), returnDate != null ? returnDate.toString() : null, tripType, adultAmount, childAmount, infantAmount, seatClass);
        if (fetchInquiry.isPresent()) {
            log.info("PRESENT");
            inquiry = fetchInquiry.get();
        } else {
            log.info("NOT PRESENT");
            Inquiry createInquiry = new Inquiry();
            createInquiry.setAirlinesCode(airlinesCode);
            createInquiry.setAdultAmount(adultAmount);
            createInquiry.setChildAmount(childAmount);
            createInquiry.setDepartureDate(departureDate);
            createInquiry.setDestinationAirportId(destinationAirportId);
            createInquiry.setInfantAmount(infantAmount);
            createInquiry.setReturnDate(returnDate);
            createInquiry.setSeatClass(seatClass);
            createInquiry.setTripType(tripType);
            createInquiry.setOriginAirportId(originAirportId);
            inquiry = inquiryRepository.save(createInquiry);
        }

        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setInquiry(inquiryMapper.inquiryToInquiryDto(inquiry));
        List<AirlinesAvailability> airlinesAvailabilities = airlinesAvailabilityRepository.getAirlinesAvailability(
                airlinesCode, originAirportId, destinationAirportId, departureDate.toString()
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
                .tgl_dep(departureDate.toString())
                .tgl_ret(returnDate != null ? returnDate.toString() : null)
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
