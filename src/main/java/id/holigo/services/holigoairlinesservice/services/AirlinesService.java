package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.web.model.*;

public interface AirlinesService {

    ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException;

    AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId);

    void saveAvailabilities(ListAvailabilityDto listAvailabilityDto);
}
