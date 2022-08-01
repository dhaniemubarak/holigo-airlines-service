package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.*;

import java.util.List;

public interface AirlinesService {

    ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException;

    AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId);

    AirlinesTransaction createBook(Long airlinesTransactionId) throws JsonProcessingException;

    void saveAvailabilities(ListAvailabilityDto listAvailabilityDto);
}
