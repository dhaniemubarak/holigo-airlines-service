package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirlinesServiceImpl implements AirlinesService {

    private RetrossAirlinesService retrossAirlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;


    @Autowired
    public void setRetrossAirlinesService(RetrossAirlinesService retrossAirlinesService) {
        this.retrossAirlinesService = retrossAirlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityRepository(AirlinesAvailabilityRepository airlinesAvailabilityRepository) {
        this.airlinesAvailabilityRepository = airlinesAvailabilityRepository;
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
    public void saveAvailabilities(ListAvailabilityDto listAvailabilityDto) {

        List<AirlinesAvailability> airlinesAvailabilities = listAvailabilityDto.getDepartures().stream().map(airlinesAvailabilityMapper::airlinesAvailabilityDtoToAirlinesAvailability).toList();

        airlinesAvailabilityRepository.saveAll(airlinesAvailabilities);
    }
}
