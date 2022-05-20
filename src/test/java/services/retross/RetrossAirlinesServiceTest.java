package services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossFlightDto;
import id.holigo.services.holigoairlinesservice.web.model.RetrossScheduleDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class RetrossAirlinesServiceTest {

    RequestScheduleDto requestScheduleDto;

    @Autowired
    RetrossAirlinesService retrossAirlinesService;

    private

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
        requestScheduleDto.setTgl_dep("2022-05-25");
        requestScheduleDto.setAdt(1);
        requestScheduleDto.setChd(0);
        requestScheduleDto.setInf(0);
        requestScheduleDto.setCabin("E");

    }

    @Test
    void getSchedule() throws JsonProcessingException {
        setUp();
        ResponseScheduleDto getSchedule = retrossAirlinesService.getSchedule(requestScheduleDto);
        log.info("Result -> {}", getSchedule.getSchedule().getDepartures());

        getSchedule.getSchedule().getDepartures().forEach(departure -> {
            for (RetrossFlightDto flights : departure.getFlights()) {
                log.info("Flight Number -> {}", flights.getFlightNumber());
            }
        });
//        RetrossScheduleDto retrossScheduleDto = getSchedule.getSchedule();
//        log.info("retrossScheduleDto -> {}", retrossScheduleDto);
        assertEquals("000", getSchedule.getError_code());
    }
}