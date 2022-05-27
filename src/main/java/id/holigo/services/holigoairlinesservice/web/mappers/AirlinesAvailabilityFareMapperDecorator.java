package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public abstract class AirlinesAvailabilityFareMapperDecorator implements AirlinesAvailabilityFareMapper {

    public AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper;

    @Autowired
    public void setAirlinesAvailabilityFareMapper(AirlinesAvailabilityFareMapper airlinesAvailabilityFareMapper) {
        this.airlinesAvailabilityFareMapper = airlinesAvailabilityFareMapper;
    }

    @Override
    public AirlinesAvailabilityFareDto retrossFareToAirlinesAvailabilityFareDto(RetrossFareDto fare) {
        AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = this.airlinesAvailabilityFareMapper.retrossFareToAirlinesAvailabilityFareDto(fare);
        airlinesAvailabilityFareDto.setFareAmount(airlinesAvailabilityFareDto.getFareAmount().setScale(2, RoundingMode.UP));
        airlinesAvailabilityFareDto.setNtaAmount(airlinesAvailabilityFareDto.getNtaAmount().setScale(2, RoundingMode.UP));
        airlinesAvailabilityFareDto.setNraAmount(airlinesAvailabilityFareDto.getFareAmount().subtract(airlinesAvailabilityFareDto.getNtaAmount()).setScale(2, RoundingMode.UP));
        return airlinesAvailabilityFareDto;
    }

    @Override
    public AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFareDto airlinesAvailabilityFareDto) {
        return airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(airlinesAvailabilityFareDto);
    }
}
