package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.ListAvailabilityDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AirlinesServiceImplTest {

    RequestScheduleDto requestScheduleDto;

    @Autowired
    AirlinesService airlinesService;

    @BeforeEach
    void setUp() {
        requestScheduleDto = new RequestScheduleDto();
        requestScheduleDto.setRqid("T35Hd6624jbadlA2hbfSFsg356gDPfgr6d4P1N02");
        requestScheduleDto.setMmid("retross_01");
        requestScheduleDto.setApp("information");
        requestScheduleDto.setAction("get_schedule");
        requestScheduleDto.setAc("JT");
        requestScheduleDto.setOrg("CGK");
        requestScheduleDto.setDes("DPS");
        requestScheduleDto.setFlight("O");
        requestScheduleDto.setTgl_dep("2022-05-28");
        requestScheduleDto.setAdt(1);
        requestScheduleDto.setChd(0);
        requestScheduleDto.setInf(0);
        requestScheduleDto.setCabin("E");
    }

    @Test
    void getAvailabilities() throws JsonProcessingException {
        ListAvailabilityDto listAvailabilityDto =
                airlinesService.getAvailabilities(requestScheduleDto);
        listAvailabilityDto.getDepartures().forEach(dto -> {
            assertEquals(requestScheduleDto.getTgl_dep(), dto.getDepartureDate().toString());
            assertEquals(requestScheduleDto.getOrg(), dto.getOriginAirportId());
            assertEquals(requestScheduleDto.getDes(), dto.getDestinationAirportId());
        });
    }
}