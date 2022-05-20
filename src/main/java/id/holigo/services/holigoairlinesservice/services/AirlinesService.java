package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.web.model.*;

public interface AirlinesService {

    ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException;

    AirlinesFinalFareDto createFinalFares(TripDto tripDto, Long userId) throws JsonProcessingException;

    AirlinesTransactionDto createTransaction(AirlinesBookDto airlinesBookDto, Long userId);

    void saveAvailabilities(ListAvailabilityDto listAvailabilityDto);
}
