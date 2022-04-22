package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class RetrossAirlinesServiceImpl implements RetrossAirlinesService {

    @Autowired
    private RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException {
        log.info("Calling get schedule with request -> {}", requestScheduleDto.getMmid());
//
        ResponseScheduleDto responseScheduleDto = new ResponseScheduleDto();
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.getSchedule(requestScheduleDto);
//
        responseScheduleDto = objectMapper.readValue(responseEntity.getBody(), ResponseScheduleDto.class);

        log.info("ResponseEntity body -> {}", responseEntity.getBody());

        return responseScheduleDto;
    }
}
