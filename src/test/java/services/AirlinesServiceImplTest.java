package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.domain.Inquiry;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.AirlinesServiceImpl;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.InquiryDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AirlinesServiceImplTest {

    InquiryDto inquiryDto;


    @Autowired
    AirlinesService airlinesService;


    @BeforeEach
    void setUp() {
        inquiryDto = new InquiryDto();
        inquiryDto.setAirlinesCode("JT");
        inquiryDto.setOriginAirportId("CGK");
        inquiryDto.setDestinationAirportId("DPS");
        inquiryDto.setTripType("O");
        inquiryDto.setDepartureDate(Date.valueOf("2022-06-03"));
        inquiryDto.setAdultAmount(1);
        inquiryDto.setChildAmount(0);
        inquiryDto.setInfantAmount(0);
        inquiryDto.setSeatClass("E");
    }

    @Test
    void getAvailabilities() throws JsonProcessingException {
        ListAvailabilityDto listAvailabilityDto =
                airlinesService.getAvailabilities(inquiryDto);
        listAvailabilityDto.getDepartures().forEach(dto -> {
            assertEquals(inquiryDto.getDepartureDate().toString(), dto.getDepartureDate().toString());
            assertEquals(inquiryDto.getOriginAirportId(), dto.getOriginAirportId());
            assertEquals(inquiryDto.getDestinationAirportId(), dto.getDestinationAirportId());
        });
    }
}