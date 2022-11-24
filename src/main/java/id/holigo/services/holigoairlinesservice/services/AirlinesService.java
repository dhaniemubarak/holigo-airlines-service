package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.*;

public interface AirlinesService {

    ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException;

    AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId);

    AirlinesFinalFare createInternationalFinalFare(RequestFinalFareDto requestFinalFareDto, long userId);

    AirlinesTransaction createBook(Long airlinesTransactionId) throws JsonProcessingException;

    void cancelBook(AirlinesTransaction airlinesTransaction) throws JsonProcessingException;

    void issued(AirlinesTransaction airlinesTransaction) throws JsonProcessingException;

    void saveAvailabilities(ListAvailabilityDto listAvailabilityDto);
}
