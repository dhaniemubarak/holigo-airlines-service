package id.holigo.services.holigoairlinesservice.services.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.TransactionDto;

import javax.jms.JMSException;

public interface TransactionService {
    TransactionDto createNewTransaction(TransactionDto transactionDto) throws JMSException, JsonProcessingException;
}
