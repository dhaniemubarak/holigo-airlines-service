package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossInternationalFareDto;
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
        if (airlinesAvailabilityFareDto.getNtaAmount().setScale(2, RoundingMode.UP).equals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP))) {
            airlinesAvailabilityFareDto.setNtaAmount(airlinesAvailabilityFareDto.getFareAmount().setScale(2, RoundingMode.UP));
        }
        airlinesAvailabilityFareDto.setNraAmount(airlinesAvailabilityFareDto.getFareAmount().subtract(airlinesAvailabilityFareDto.getNtaAmount()).setScale(2, RoundingMode.UP));
        if (fare.getSelectedIdDep() != null) {
            airlinesAvailabilityFareDto.setSelectedId(fare.getSelectedIdDep());
        }
        if (fare.getSelectedIdRet() != null) {
            airlinesAvailabilityFareDto.setSelectedId(fare.getSelectedIdRet());
        }
        return airlinesAvailabilityFareDto;
    }

    @Override
    public AirlinesAvailabilityFareDto retrossInternationalFareToAirlinesAvailabilityFareDto(RetrossInternationalFareDto fare){
        AirlinesAvailabilityFareDto airlinesAvailabilityFareDto = this.airlinesAvailabilityFareMapper.retrossInternationalFareToAirlinesAvailabilityFareDto(fare);
        airlinesAvailabilityFareDto.setFareAmount(airlinesAvailabilityFareDto.getFareAmount().setScale(2, RoundingMode.UP));
        airlinesAvailabilityFareDto.setNtaAmount(airlinesAvailabilityFareDto.getNtaAmount().setScale(2, RoundingMode.UP));
        if (airlinesAvailabilityFareDto.getNtaAmount().setScale(2, RoundingMode.UP).equals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP))) {
            airlinesAvailabilityFareDto.setNtaAmount(airlinesAvailabilityFareDto.getFareAmount().setScale(2, RoundingMode.UP));
        }
        airlinesAvailabilityFareDto.setNraAmount(airlinesAvailabilityFareDto.getFareAmount().subtract(airlinesAvailabilityFareDto.getNtaAmount()).setScale(2, RoundingMode.UP));
        if (fare.getSelectedIdDep() != null) {
            airlinesAvailabilityFareDto.setSelectedId(fare.getSelectedIdDep());
        }
        if (fare.getSelectedIdRet() != null) {
            airlinesAvailabilityFareDto.setSelectedId(fare.getSelectedIdRet());
        }
        return airlinesAvailabilityFareDto;
    }

    @Override
    public AirlinesAvailabilityFareDto airlinesAvailabilityItineraryToAirlinesAvailabilityFareDto(AirlinesAvailabilityItinerary airlinesAvailabilityItinerary) {
        return airlinesAvailabilityFareMapper.airlinesAvailabilityItineraryToAirlinesAvailabilityFareDto(airlinesAvailabilityItinerary);
    }

    @Override
    public AirlinesAvailabilityFareDto airlinesAvailabilityFareToAirlinesAvailabilityFareDto(AirlinesAvailabilityFare airlinesAvailabilityFare) {
        return airlinesAvailabilityFareMapper.airlinesAvailabilityFareToAirlinesAvailabilityFareDto(airlinesAvailabilityFare);
    }
}
