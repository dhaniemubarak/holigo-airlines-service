package id.holigo.services.holigoairlinesservice.services.fare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;

@Slf4j
@RequiredArgsConstructor
public class FareServiceImpl implements FareService {

    private JmsTemplate jmsTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public FareDto getFareDetail(FareDetailDto fareDetailDto) {
        Message message = jmsTemplate.sendAndReceive(JmsConfig.GET_DETAIL_FARE_PRODUCT, session -> {
            Message detailFareMessage;
            try {
                detailFareMessage = session.createTextMessage(objectMapper.writeValueAsString(fareDetailDto));
                detailFareMessage.setStringProperty("_type", "id.holigo.services.common.model.FareDetailDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return detailFareMessage;
        });

        assert message != null;
        try {
            return objectMapper.readValue(message.getBody(String.class), FareDto.class);
        } catch (JsonProcessingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
