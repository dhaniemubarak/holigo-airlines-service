package id.holigo.services.holigoairlinesservice.services.fare;

import id.holigo.services.common.model.FareDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "holigo-fare-service")
public interface FareServiceFeignClient {

    String FARE_DETAIL = "/api/v1/fare";

    @RequestMapping(method = RequestMethod.GET, value = FARE_DETAIL)
    ResponseEntity<FareDto> getFare(@RequestParam Long userId,
                                    @RequestParam Integer productId,
                                    @RequestParam BigDecimal nraAmount,
                                    @RequestParam BigDecimal ntaAmount);
}
