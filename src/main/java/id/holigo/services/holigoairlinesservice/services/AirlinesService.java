package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;

import java.util.List;

public interface AirlinesService {

    ListAvailabilityDto getAvailabilities(RequestScheduleDto requestScheduleDto) throws JsonProcessingException;

    void saveAvailabilities(List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList);
}
