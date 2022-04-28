package id.holigo.services.holigoairlinesservice.services.fare;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;

import javax.jms.JMSException;

public interface FareService {

    FareDto getFareDetail(FareDetailDto fareDetailDto) throws JMSException, JsonProcessingException;
}
