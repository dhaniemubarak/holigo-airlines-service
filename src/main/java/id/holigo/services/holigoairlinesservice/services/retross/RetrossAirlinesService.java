package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.web.model.*;

public interface RetrossAirlinesService {

    ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException;

    ResponseFareDto getFare(TripDto tripDto) throws JsonProcessingException;
}
