package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.*;

import java.util.HashMap;
import java.util.Map;

public interface RetrossAirlinesService {

    ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException;

    ResponseFareDto getFare(TripDto tripDto, Map<String, String> roundTrip) throws JsonProcessingException;

    ResponseBookDto createBook(AirlinesTransaction airlinesTransaction) throws JsonProcessingException;
}
