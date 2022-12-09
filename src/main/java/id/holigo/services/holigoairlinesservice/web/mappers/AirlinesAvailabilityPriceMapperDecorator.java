package id.holigo.services.holigoairlinesservice.web.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailabilityItinerary;
import id.holigo.services.holigoairlinesservice.services.fare.FareService;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityFareDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityPriceDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class AirlinesAvailabilityPriceMapperDecorator implements AirlinesAvailabilityPriceMapper {

    private FareService fareService;
    private AirlinesAvailabilityPriceMapper airlinesAvailabilityPriceMapper;

    @Autowired
    public void setAirlinesAvailabilityPriceMapper(AirlinesAvailabilityPriceMapper airlinesAvailabilityPriceMapper) {
        this.airlinesAvailabilityPriceMapper = airlinesAvailabilityPriceMapper;
    }

    @Autowired
    public void setFareService(FareService fareService) {
        this.fareService = fareService;
    }

    @Override
    public AirlinesAvailabilityPriceDto airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(
            AirlinesAvailabilityFareDto airlinesAvailabilityFareDto, Long userId) {
        AirlinesAvailabilityPriceDto airlinesAvailabilityPriceDto = this.airlinesAvailabilityPriceMapper
                .airlinesAvailabilityFareDtoToAirlinesAvailabilityPriceDto(airlinesAvailabilityFareDto, userId);
        FareDetailDto fareDetailDto = FareDetailDto.builder()
                .userId(userId)
                .productId(1)
                .nraAmount(airlinesAvailabilityFareDto.getNraAmount())
                .ntaAmount(airlinesAvailabilityFareDto.getNtaAmount()).build();
        FareDto fareDto;
        fareDto = fareService.getFareDetail(fareDetailDto);
        assert fareDto != null;
        airlinesAvailabilityPriceDto.setNormalFare(airlinesAvailabilityFareDto.getFareAmount());
        airlinesAvailabilityPriceDto.setHpAmount(fareDto.getHpAmount());
        airlinesAvailabilityPriceDto.setHpcAmount(fareDto.getHpcAmount());
        airlinesAvailabilityPriceDto.setFareAmount(fareDto.getFareAmount());

        return airlinesAvailabilityPriceDto;
    }

    @Override
    public AirlinesAvailabilityPriceDto airlinesAvailabilityItineraryToAirlinesAvailabilityPriceDto(
            AirlinesAvailabilityItinerary airlinesAvailabilityItinerary, Long userId) {
        AirlinesAvailabilityPriceDto airlinesAvailabilityPriceDto = airlinesAvailabilityPriceMapper
                .airlinesAvailabilityItineraryToAirlinesAvailabilityPriceDto(airlinesAvailabilityItinerary, userId);
        FareDetailDto fareDetailDto = FareDetailDto.builder()
                .userId(userId)
                .productId(1)
                .nraAmount(airlinesAvailabilityItinerary.getNraAmount())
                .ntaAmount(airlinesAvailabilityItinerary.getNtaAmount()).build();
        FareDto fareDto;
        fareDto = fareService.getFareDetail(fareDetailDto);
        assert fareDto != null;
        airlinesAvailabilityPriceDto.setNormalFare(airlinesAvailabilityItinerary.getNormalFare());
        airlinesAvailabilityPriceDto.setHpAmount(fareDto.getHpAmount());
        airlinesAvailabilityPriceDto.setHpcAmount(fareDto.getHpcAmount());
        airlinesAvailabilityPriceDto.setFareAmount(fareDto.getFareAmount());
        return airlinesAvailabilityPriceDto;
    }
}
