package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AirlinesServiceImpl implements AirlinesService {

    private RetrossAirlinesService retrossAirlinesService;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;


    @Autowired
    public void setRetrossAirlinesService(RetrossAirlinesService retrossAirlinesService) {
        this.retrossAirlinesService = retrossAirlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityMapper(
            AirlinesAvailabilityMapper airlinesAvailabilityMapper) {
        this.airlinesAvailabilityMapper = airlinesAvailabilityMapper;
    }

    @Override
    public ListAvailabilityDto getAvailabilities(RequestScheduleDto requestScheduleDto) throws JsonProcessingException {
        ResponseScheduleDto responseScheduleDto = retrossAirlinesService.getSchedule(requestScheduleDto);
        if (responseScheduleDto.getSchedule() == null) {
            return null;
        }
        return airlinesAvailabilityMapper.responseScheduleDtoToListAvailabilityDto(responseScheduleDto);
    }

    @Override
    public void saveAvailabilities(List<AirlinesAvailabilityDto> airlinesAvailabilityDtoList) {

    }
}
