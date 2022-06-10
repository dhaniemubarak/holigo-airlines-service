package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.repositories.InquiryRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.exceptions.AvailabilitiesException;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.InquiryMapper;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
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
    public ResponseEntity<ListAvailabilityDto> getAvailabilities(InquiryDto inquiryDto, @RequestHeader("user-id") Long userId) {
        inquiryDto.setUserId(userId);
        Inquiry inquiry;
        List<AirlinesAvailabilityDto> availabilityDeparturesDto = new ArrayList<>();
        Optional<Inquiry> fetchInquiry = inquiryRepository.getInquiry(inquiryDto.getAirlinesCode(),
                inquiryDto.getOriginAirportId(), inquiryDto.getDestinationAirportId(), inquiryDto.getDepartureDate().toString(),
                inquiryDto.getReturnDate() != null ? inquiryDto.getReturnDate().toString() : null, inquiryDto.getTripType().toString(),
                inquiryDto.getAdultAmount(), inquiryDto.getChildAmount(), inquiryDto.getInfantAmount(), inquiryDto.getSeatClass());

        if (fetchInquiry.isEmpty()) {
            try {
                inquiry = inquiryRepository.save(inquiryMapper.inquiryDtoToInquiry(inquiryDto));
                inquiryDto.setId(inquiry.getId());
            } catch (Exception e) {
                // TODO Need message
                throw new AvailabilitiesException();
            }

        } else {
            inquiry = fetchInquiry.get();
            inquiryDto.setId(inquiry.getId());
        }

        ListAvailabilityDto listAvailabilityDto = new ListAvailabilityDto();
        listAvailabilityDto.setInquiry(inquiryDto);

        List<AirlinesAvailability> airlinesAvailabilityDepartures = airlinesAvailabilityRepository.getAirlinesAvailability(
                inquiry.getAirlinesCode(), inquiry.getOriginAirportId(), inquiry.getDestinationAirportId(),
                inquiry.getDepartureDate().toString()
        );

        if (airlinesAvailabilityDepartures.size() > 0) {
            listAvailabilityDto.setDepartures(airlinesAvailabilityDepartures.stream().map(airlinesAvailabilityMapper::airlinesAvailabilityToAirlinesAvailabilityDto).toList());
        }

        if (inquiry.getTripType() == TripType.R) {
            List<AirlinesAvailability> airlinesAvailabilityReturns = airlinesAvailabilityRepository.getAirlinesAvailability(
                    inquiry.getAirlinesCode(), inquiry.getDestinationAirportId(), inquiry.getOriginAirportId(),
                    inquiry.getReturnDate().toString()
            );

            if (airlinesAvailabilityReturns.size() > 0) {
                listAvailabilityDto.setReturns(airlinesAvailabilityReturns.stream().map(airlinesAvailabilityMapper::airlinesAvailabilityToAirlinesAvailabilityDto).toList());
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
