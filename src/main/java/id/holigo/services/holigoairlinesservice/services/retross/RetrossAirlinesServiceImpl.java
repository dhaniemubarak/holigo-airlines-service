package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class RetrossAirlinesServiceImpl implements RetrossAirlinesService {

    private static final String RETROSS_ID = "holigo";

    private static final String RETROSS_PASSKEY = "H0LJSHRG3754875Y4698NKJWEF8UHIGO";

    @Autowired
    private RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException {
        requestScheduleDto.setMmid(RETROSS_ID);
        requestScheduleDto.setRqid(RETROSS_PASSKEY);
        requestScheduleDto.setApp("information");
        requestScheduleDto.setAction("get_schedule");
        log.info("Calling get schedule with request -> {}", requestScheduleDto.getMmid());
//
        ResponseScheduleDto responseScheduleDto;
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.getSchedule(requestScheduleDto);
//
        responseScheduleDto = objectMapper.readValue(responseEntity.getBody(), ResponseScheduleDto.class);

        log.info("ResponseEntity body -> {}", responseEntity.getBody());

        return responseScheduleDto;
    }

    @Override
    public ResponseFareDto getFare(TripDto tripDto) throws JsonProcessingException {
        RequestFareDto requestFareDto = RequestFareDto.builder()
                .mmid(RETROSS_ID)
                .rqid(RETROSS_PASSKEY)
                .app("information")
                .action("get_fare")
                .acDep(tripDto.getInquiry().getAirlinesCode())
                .org(tripDto.getInquiry().getOriginAirportId())
                .des(tripDto.getInquiry().getDestinationAirportId())
                .tgl_dep(tripDto.getInquiry().getDepartureDate().toString())
                .tgl_ret(tripDto.getInquiry().getReturnDate() != null ? tripDto.getInquiry().getReturnDate().toString() : null)
                .flight(tripDto.getInquiry().getTripType().toString())
                .adt(tripDto.getInquiry().getAdultAmount())
                .chd(tripDto.getInquiry().getChildAmount())
                .inf(tripDto.getInquiry().getInfantAmount())
                .selectedIdDep(tripDto.getTrip().getFare().getSelectedId()).build();
        ResponseFareDto responseFareDto;

        log.info("Calling get fare with request -> {}", objectMapper.writeValueAsString(requestFareDto));

        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.getFare(requestFareDto);

        log.info("ResponseEntity body -> {}", responseEntity.getBody());
//
        responseFareDto = objectMapper.readValue(responseEntity.getBody(), ResponseFareDto.class);
        return responseFareDto;
    }
}
