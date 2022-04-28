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
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Slf4j
@RequiredArgsConstructor
@Service
public class FareServiceImpl implements FareService {

    private JmsTemplate jmsTemplate;

    private ObjectMapper objectMapper;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public FareDto getFareDetail(FareDetailDto fareDetailDto) throws JMSException, JsonProcessingException {
        log.info("JMS getFareDetail is running...");
        log.info("fareDetailDto -> {}", fareDetailDto);
        Message message = jmsTemplate.sendAndReceive(JmsConfig.GET_DETAIL_FARE_PRODUCT, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message detailFareMessage = null;
                try {
                    detailFareMessage = session.createTextMessage(objectMapper.writeValueAsString(fareDetailDto));
                    detailFareMessage.setStringProperty("_type", "id.holigo.services.common.model.FareDetailDto");
                } catch (JsonProcessingException e) {
                    throw new JMSException(e.getMessage());
                }
                return detailFareMessage;
            }
        });
        FareDto fareDto = objectMapper.readValue(message.getBody(String.class), FareDto.class);

        return fareDto;
    }
}
