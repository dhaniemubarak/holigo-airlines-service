package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;

public interface AirlinesTransactionService {

    TransactionDto createTransaction(AirlinesBookDto airlinesBookDto, Long userId);
}
