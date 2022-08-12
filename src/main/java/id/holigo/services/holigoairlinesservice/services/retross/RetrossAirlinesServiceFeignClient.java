package id.holigo.services.holigoairlinesservice.services.retross;

import id.holigo.services.holigoairlinesservice.web.model.RequestBookDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestFareDto;
import id.holigo.services.holigoairlinesservice.web.model.RequestScheduleDto;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

@FeignClient(name = "retross-airlines", url = "http://ws.retross.com")
public interface RetrossAirlinesServiceFeignClient {

    public static final String DOMESTIC_URI = "/airline/domestik/";

    public static final String INTERNATIONAL_URI = "/airline/international/";

    @RequestMapping(method = RequestMethod.POST, value = DOMESTIC_URI)
    ResponseEntity<String> getSchedule(@RequestBody RequestScheduleDto requestScheduleDto);

    @RequestMapping(method = RequestMethod.POST, value = INTERNATIONAL_URI)
    ResponseEntity<String> getInternationalSchedule(@RequestBody RequestScheduleDto requestScheduleDto);

    @RequestMapping(method = RequestMethod.POST, value = DOMESTIC_URI)
    ResponseEntity<String> getFare(@RequestBody RequestFareDto requestFareDto);

    @RequestMapping(method = RequestMethod.POST, value = INTERNATIONAL_URI)
    ResponseEntity<String> getInternationalFare(@RequestBody RequestFareDto requestFareDto);

    @RequestMapping(method = RequestMethod.POST, value = DOMESTIC_URI)
    ResponseEntity<String> book(@RequestBody String request);

    @RequestMapping(method = RequestMethod.POST, value = DOMESTIC_URI)
    ResponseEntity<String> cancel(@RequestBody String request);

    @RequestMapping(method = RequestMethod.POST, value = DOMESTIC_URI)
    ResponseEntity<String> issued(@RequestBody String request);
}
