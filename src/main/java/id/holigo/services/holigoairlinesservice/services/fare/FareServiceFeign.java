package id.holigo.services.holigoairlinesservice.services.fare;

import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FareServiceFeign implements FareService {

    private final FareServiceFeignClient fareServiceFeignClient;

    @Override
    public FareDto getFareDetail(FareDetailDto fareDetailDto) {
        log.info("Calling fare service feign with");

        ResponseEntity<FareDto> responseEntity = fareServiceFeignClient.getFare(
                fareDetailDto.getUserId(),
                fareDetailDto.getProductId(),
                fareDetailDto.getNraAmount(),
                fareDetailDto.getNtaAmount());

        log.info("responseEntity body -> {}", responseEntity.getBody());

        return responseEntity.getBody();
    }
}
