package id.holigo.services.holigoairlinesservice.services.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.events.TransactionEvent;
import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private ObjectMapper objectMapper;
    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public TransactionDto createNewTransaction(TransactionDto transactionDto) throws JMSException, JsonProcessingException {
        Message received = jmsTemplate.sendAndReceive(JmsConfig.CREATE_NEW_TRANSACTION, session -> {
            Message data;
            try {
                data = session.createTextMessage(objectMapper.writeValueAsString(transactionDto));
                data.setStringProperty("_type", "id.holigo.services.common.model.TransactionDto");
            } catch (JsonProcessingException e) {
                throw new JMSException(e.getMessage());
            }
            return data;
        });
        assert received != null;
        return objectMapper.readValue(received.getBody(String.class), TransactionDto.class);
    }

    @Override
    public void updateOrderStatusTransaction(TransactionDto transactionDto) {
        jmsTemplate.convertAndSend(JmsConfig.SET_ORDER_STATUS_BY_TRANSACTION_ID_TYPE, new TransactionEvent(transactionDto));
    }
}
