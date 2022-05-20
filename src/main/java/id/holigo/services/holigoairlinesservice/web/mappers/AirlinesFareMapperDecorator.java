package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.domain.AirlinesFinalFare;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesFinalFareRepository;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesFinalFareDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseFareDto;
import id.holigo.services.holigoairlinesservice.web.model.TripDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Slf4j
public abstract class AirlinesFareMapperDecorator implements AirlinesFareMapper {

    private InquiryMapper inquiryMapper;
    private AirlinesFinalFareRepository airlinesFinalFareRepository;
    private AirlinesFareMapper airlinesFareMapper;

    @Autowired
    public void setAirlinesFareMapper(AirlinesFareMapper airlinesFareMapper) {
        this.airlinesFareMapper = airlinesFareMapper;
    }

    @Autowired
    public void setAirlinesFinalFareRepository(AirlinesFinalFareRepository airlinesFinalFareRepository) {
        this.airlinesFinalFareRepository = airlinesFinalFareRepository;
    }

    @Autowired
    public void setInquiryMapper(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }

    @Override
    public AirlinesFinalFare responseFareDtoToAirlinesFinalFare(ResponseFareDto responseFareDto, TripDto tripDto, Long userId) {
        AirlinesFinalFare airlinesFinalFare = new AirlinesFinalFare();
        airlinesFinalFare.setUserId(userId);
        airlinesFinalFare.setFareAmount(responseFareDto.getTotalAmount());
        airlinesFinalFare.setNtaAmount(responseFareDto.getNtaAmount());
        airlinesFinalFare.setIsBookable(true);
        airlinesFinalFare.setNraAmount(airlinesFinalFare.getFareAmount().subtract(airlinesFinalFare.getNtaAmount()));
        id.holigo.services.common.model.FareDto fareDto = FareDto.builder().productId(1)
                .ntaAmount(airlinesFinalFare.getNtaAmount())
                .nraAmount(airlinesFinalFare.getNraAmount())
                .fareAmount(airlinesFinalFare.getFareAmount())
                .cpAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.55)))
                .ipAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.2)))
                .mpAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.1)))
                .hpAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.05)))
                .prAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.05)))
                .hvAmount(airlinesFinalFare.getNraAmount().multiply(BigDecimal.valueOf(0.05)))
                .ipcAmount(BigDecimal.valueOf(0.00))
                .hpcAmount(BigDecimal.valueOf(0.00))
                .prcAmount(BigDecimal.valueOf(0.00))
                .lossAmount(BigDecimal.valueOf(0.00)).build();
        airlinesFinalFare.setAdminAmount(BigDecimal.valueOf(0.00));
        airlinesFinalFare.setCpAmount(fareDto.getCpAmount());
        airlinesFinalFare.setMpAmount(fareDto.getMpAmount());
        airlinesFinalFare.setIpAmount(fareDto.getIpAmount());
        airlinesFinalFare.setHpAmount(fareDto.getHpAmount());
        airlinesFinalFare.setHvAmount(fareDto.getHvAmount());
        airlinesFinalFare.setPrAmount(fareDto.getPrAmount());
        airlinesFinalFare.setIpcAmount(fareDto.getIpcAmount());
        airlinesFinalFare.setHpcAmount(fareDto.getHpcAmount());
        airlinesFinalFare.setPrcAmount(fareDto.getPrcAmount());
        airlinesFinalFare.setLossAmount(fareDto.getLossAmount());
        airlinesFinalFare.setInquiry(inquiryMapper.inquiryDtoToInquiry(tripDto.getInquiry()));
        return airlinesFinalFareRepository.save(airlinesFinalFare);
    }

    @Override
    public AirlinesFinalFareDto airlinesFinalFareToAirlinesFinalFareDto(AirlinesFinalFare airlinesFinalFare) {
        return this.airlinesFareMapper.airlinesFinalFareToAirlinesFinalFareDto(airlinesFinalFare);
    }

    @Override
    public AirlinesTransaction airlinesFinalFareToAirlinesTransaction(AirlinesFinalFare airlinesFinalFare) {
        return this.airlinesFareMapper.airlinesFinalFareToAirlinesTransaction(airlinesFinalFare);
    }
}
