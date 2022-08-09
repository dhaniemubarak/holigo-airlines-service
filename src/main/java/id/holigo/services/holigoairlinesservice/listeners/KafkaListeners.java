package id.holigo.services.holigoairlinesservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.config.KafkaTopicConfig;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.domain.IssuedError;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.IssuedErrorRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.PaymentAirlinesTransactionService;
import id.holigo.services.common.model.AirlinesTransactionDtoForUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaListeners {

    private final AirlinesTransactionRepository airlinesTransactionRepository;

    private final IssuedErrorRepository issuedErrorRepository;
    private final AirlinesService airlinesService;

    private final PaymentAirlinesTransactionService paymentAirlinesTransactionService;

    private final OrderAirlinesTransactionService orderAirlinesTransactionService;

    private final TransactionTemplate transactionTemplate;

    @KafkaListener(topics = KafkaTopicConfig.UPDATE_AIRLINES_TRANSACTION, groupId = "airlines-transaction-expired", containerFactory = "airlinesTransactionListenerFactory")
    void expirationListener(AirlinesTransactionDtoForUser airlinesTransactionDto) {
        log.info("expirationListener is running ....");
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@Nullable TransactionStatus status) {
                Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(airlinesTransactionDto.getId());
                if (fetchAirlinesTransaction.isPresent()) {
                    AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
                    if (airlinesTransactionDto.getPaymentStatus().equals(PaymentStatusEnum.PAID)) {
                        paymentAirlinesTransactionService.paymentHasPaid(airlinesTransaction.getId());
                    } else {
                        paymentAirlinesTransactionService.paymentHasExpired(airlinesTransaction.getId());
                        orderAirlinesTransactionService.orderHasExpired(airlinesTransaction.getId());
                    }
                }
            }
        });
    }
}
