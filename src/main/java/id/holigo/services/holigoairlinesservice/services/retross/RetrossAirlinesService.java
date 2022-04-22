package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;

public interface RetrossAirlinesService {

    ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException;
}
