package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
public abstract class AirlinesAvailabilityFareMapperDecorator implements AirlinesAvailabilityFareMapper {

    public AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper;

    @Autowired
    public void setAirlinesAvailabilityFareMapper(AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper) {
        this.airlinesAvailabilityFareMapper = airlinesAvailabilityFareMapper;
    }

    @Override
    public AirlinesAvailabilityFareDto retrossFareToAirlinesAvailabilityFareDto(RetrossFareDto fare) {
        return airlinesAvailabilityFareMapper.retrossFareToAirlinesAvailabilityFareDto(fare);
    }

    @Override
    public AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto) {
        return airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(airlinesAvailabilityFareDto);
    }
}
