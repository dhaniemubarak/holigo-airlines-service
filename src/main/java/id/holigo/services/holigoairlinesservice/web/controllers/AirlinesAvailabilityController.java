package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.Airport;
import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirportRepository;
import id.holigo.services.holigoairlinesservice.repositories.InquiryRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.exceptions.AvailabilitiesException;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.InquiryMapper;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AirlinesAvailabilityController {

    private AirlinesService airlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private InquiryRepository inquiryRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private InquiryMapper inquiryMapper;

    private AirportRepository airportRepository;


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

    @Autowired
    public void setAirportRepository(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @GetMapping("/api/v1/airlines/availabilities")
    public ResponseEntity<ListAvailabilityDto> getAvailabilities(InquiryDto inquiryDto, @RequestHeader("user-id") Long userId) {
        Inquiry inquiry;
        List<AirlinesAvailabilityDto> availabilityDeparturesDto = new ArrayList<>();
        Optional<Inquiry> fetchInquiry = inquiryRepository.getInquiry(inquiryDto.getAirlinesCode(),
                inquiryDto.getOriginAirportId(), inquiryDto.getDestinationAirportId(), inquiryDto.getDepartureDate().toString(),
                inquiryDto.getReturnDate() != null ? inquiryDto.getReturnDate().toString() : null, inquiryDto.getTripType().toString(),
                inquiryDto.getAdultAmount(), inquiryDto.getChildAmount(), inquiryDto.getInfantAmount(), inquiryDto.getSeatClass());

        if (fetchInquiry.isEmpty()) {
            try {
                Inquiry inquiryObject = inquiryMapper.inquiryDtoToInquiry(inquiryDto);
                Airport originAirport = airportRepository.getById(inquiryDto.getOriginAirportId());
                Airport destinationAirport = airportRepository.getById(inquiryDto.getDestinationAirportId());
                inquiryObject.setOriginAirport(originAirport);
                inquiryObject.setDestinationAirport(destinationAirport);
                inquiry = inquiryRepository.save(inquiryObject);
            } catch (Exception e) {
                // TODO Need message
                throw new AvailabilitiesException();
            }

        } else {
            inquiry = fetchInquiry.get();
        }
        inquiryDto = inquiryMapper.inquiryToInquiryDto(inquiry);
        inquiryDto.setUserId(userId);
        if (!inquiryDto.getAirlinesCode().equals("IA") &&
                (!inquiryDto.getOriginAirport().getCountry().toLowerCase().equals("indonesia") ||
                        !inquiryDto.getDestinationAirport().getCountry().toLowerCase().equals("indonesia"))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setInquiry(inquiryDto);

        List<AirlinesAvailability> airlinesAvailabilityDepartures = airlinesAvailabilityRepository.getAirlinesAvailability(
                inquiry.getAirlinesCode(), inquiry.getOriginAirport().getId(), inquiry.getDestinationAirport().getId(),
                inquiry.getDepartureDate().toString()
        );
        try {
            log.info("Inquiry -> {}", new ObjectMapper().writeValueAsString(inquiryDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("size -> {}", airlinesAvailabilityDepartures.size());

        if (airlinesAvailabilityDepartures.size() > 0) {
            try {
                listAvailabilityDto.setDepartures(
                        airlinesAvailabilityDepartures.stream().map(
                                airlinesAvailability -> airlinesAvailabilityMapper.airlinesAvailabilityToAirlinesAvailabilityDto(
                                        airlinesAvailability,
                                        userId,
                                        inquiry.getAdultAmount() + inquiry.getChildAmount() + inquiry.getInfantAmount())
                        ).toList()
                );
            } catch (NoSuchElementException e) {
                airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                        inquiryDto.getAirlinesCode(),
                        inquiryDto.getOriginAirportId(),
                        inquiryDto.getDestinationAirportId(),
                        inquiryDto.getDepartureDate().toString());
                throw new AvailabilitiesException();
            }

        }

        if (inquiry.getTripType() == TripType.R) {
            List<AirlinesAvailability> airlinesAvailabilityReturns = airlinesAvailabilityRepository.getAirlinesAvailability(
                    inquiry.getAirlinesCode(), inquiry.getDestinationAirport().getId(), inquiry.getOriginAirport().getId(),
                    inquiry.getReturnDate().toString()
            );

            if (airlinesAvailabilityReturns.size() > 0) {
                try {
                    listAvailabilityDto.setReturns(airlinesAvailabilityReturns.stream().map(airlinesAvailability -> airlinesAvailabilityMapper.airlinesAvailabilityToAirlinesAvailabilityDto(airlinesAvailability, userId, inquiry.getAdultAmount() + inquiry.getChildAmount() + inquiry.getInfantAmount())).toList());
                } catch (NoSuchElementException e) {
                    airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                            inquiryDto.getAirlinesCode(),
                            inquiryDto.getDestinationAirportId(),
                            inquiryDto.getOriginAirportId(),
                            inquiryDto.getReturnDate().toString());
                    throw new AvailabilitiesException();
                }

            }
            if (airlinesAvailabilityDepartures.isEmpty() && airlinesAvailabilityReturns.isEmpty()) {

                try {
                    listAvailabilityDto = airlinesService.getAvailabilities(inquiryDto);
                } catch (JsonProcessingException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (listAvailabilityDto.getDepartures() == null || listAvailabilityDto.getReturns() == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                if (airlinesAvailabilityDepartures.isEmpty()) {
                    inquiryDto.setReturnDate(null);
                    inquiryDto.setTripType(TripType.O);
                    try {
                        listAvailabilityDto.setDepartures(airlinesService.getAvailabilities(inquiryDto).getDepartures());
                    } catch (JsonProcessingException e) {
                        throw new AvailabilitiesException();
                    }

                }
                if (airlinesAvailabilityReturns.isEmpty()) {
                    inquiryDto.setOriginAirportId(inquiry.getDestinationAirportId());
                    inquiryDto.setDestinationAirportId(inquiry.getOriginAirportId());
                    inquiryDto.setDepartureDate(inquiry.getReturnDate());
                    inquiryDto.setReturnDate(null);
                    inquiryDto.setTripType(TripType.O);
                    try {
                        listAvailabilityDto.setReturns(airlinesService.getAvailabilities(inquiryDto).getDepartures());
                    } catch (JsonProcessingException e) {
                        throw new AvailabilitiesException();
                    }
                }
            }
        } else if (airlinesAvailabilityDepartures.isEmpty()) {
            try {
                listAvailabilityDto = airlinesService.getAvailabilities(inquiryDto);
            } catch (JsonProcessingException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (listAvailabilityDto.getDepartures() == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        listAvailabilityDto.setInquiry(inquiryDto);

        listAvailabilityDto.getDepartures().forEach(departure -> {
            departure.setFares(null);
            availabilityDeparturesDto.add(departure);
        });
        listAvailabilityDto.setDepartures(availabilityDeparturesDto);
        if (listAvailabilityDto.getReturns() != null) {
            List<AirlinesAvailabilityDto> availabilityReturnsDto = new ArrayList<>();
            listAvailabilityDto.getReturns().forEach(departure -> {
                departure.setFares(null);
                availabilityReturnsDto.add(departure);
            });
            listAvailabilityDto.setReturns(availabilityReturnsDto);
        }
        return new ResponseEntity<>(listAvailabilityDto, HttpStatus.OK);
    }
}
