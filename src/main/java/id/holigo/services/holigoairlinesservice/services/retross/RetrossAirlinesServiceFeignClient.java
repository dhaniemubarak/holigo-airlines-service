package id.holigo.services.holigoairlinesservice.services.retross;

import feign.Headers;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import id.holigo.services.holigoairlinesservice.web.model.ResponseScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "retross-airlines", url = "http://ws.retross.com")
public interface RetrossAirlinesServiceFeignClient {

    public static final String GET_SCHEDULE = "/airline/domestik/";

    @RequestMapping(method = RequestMethod.POST, value = GET_SCHEDULE)
    ResponseEntity<String> getSchedule(@RequestBody RequestScheduleDto requestScheduleDto);

}
