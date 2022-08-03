package id.holigo.services.holigoairlinesservice.services.retross;

import id.holigo.services.holigoairlinesservice.web.model.RequestBookDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.GET;

@FeignClient(name = "retross-airlines", url = "http://ws.retross.com")
public interface RetrossAirlinesServiceFeignClient {

    public static final String GET_SCHEDULE = "/airline/domestik/";

    @RequestMapping(method = RequestMethod.POST, value = GET_SCHEDULE)
    ResponseEntity<String> getSchedule(@RequestBody RequestScheduleDto requestScheduleDto);

    @RequestMapping(method = RequestMethod.POST, value = GET_SCHEDULE)
    ResponseEntity<String> getFare(@RequestBody RequestFareDto requestFareDto);

    @RequestMapping(method = RequestMethod.POST, value = GET_SCHEDULE)
    ResponseEntity<String> book(@RequestBody String request);

    @RequestMapping(method = RequestMethod.POST, value = GET_SCHEDULE)
    ResponseEntity<String> cancel(@RequestBody String request);
}
