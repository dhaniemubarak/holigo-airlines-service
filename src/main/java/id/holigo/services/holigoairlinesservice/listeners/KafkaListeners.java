package id.holigo.services.holigoairlinesservice.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.config.KafkaTopicConfig;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.events.PaymentStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.IssuedErrorRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.PaymentAirlinesTransactionService;
import id.holigo.services.common.model.AirlinesTransactionDtoForUser;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.lang.Nullable;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaListeners {

    private final AirlinesTransactionRepository airlinesTransactionRepository;
    private final AirlinesService airlinesService;

    private final PaymentAirlinesTransactionService paymentAirlinesTransactionService;

    private final OrderAirlinesTransactionService orderAirlinesTransactionService;

    private final TransactionService transactionService;

    private final TransactionTemplate transactionTemplate;

    @KafkaListener(topics = KafkaTopicConfig.UPDATE_ORDER_STATUS_AIRLINES_TRANSACTION, groupId = "order-status-airlines-transaction", containerFactory = "airlinesTransactionListenerFactory")
    void updateOrderStatus(AirlinesTransactionDtoForUser airlinesTransactionDto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@Nullable TransactionStatus status) {
                Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(airlinesTransactionDto.getId());
                if (fetchAirlinesTransaction.isPresent()) {
                    AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
                    switch (airlinesTransactionDto.getOrderStatus()) {
                        case ORDER_EXPIRED -> {
                            StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.orderHasExpired(airlinesTransaction.getId());
                            if (sm.getState().getId().equals(OrderStatusEnum.ORDER_EXPIRED)) {
                                paymentAirlinesTransactionService.paymentHasExpired(airlinesTransaction.getId());
                            }
                        }
                        case PROCESS_BOOK, BOOKED, BOOK_FAILED, PROCESS_ISSUED, WAITING_ISSUED, ISSUED, RETRYING_ISSUED -> {
                            log.info("case PROCESS_BOOK, BOOKED, BOOK_FAILED, PROCESS_ISSUED, WAITING_ISSUED, ISSUED, RETRYING_ISSUED ");
                        }
                        case ISSUED_FAILED -> transactionService.updateOrderStatusTransaction(TransactionDto.builder()
                                .id(airlinesTransaction.getTransactionId())
                                .orderStatus(OrderStatusEnum.ISSUED_FAILED).build());
                        case ORDER_CANCELED -> {
                            StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.orderHasCanceled(airlinesTransaction.getId());
                            if (sm.getState().getId().equals(OrderStatusEnum.ORDER_CANCELED)) {
                                paymentAirlinesTransactionService.paymentHasCanceled(airlinesTransaction.getId());
                            }
                        }
                    }
                }
            }
        });
    }

    @Transactional
    @KafkaListener(topics = KafkaTopicConfig.UPDATE_PAYMENT_STATUS_AIRLINES_TRANSACTION, groupId = "payment-status-airlines-transaction", containerFactory = "airlinesTransactionListenerFactory")
    void updatePaymentStatus(AirlinesTransactionDtoForUser airlinesTransactionDto) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@Nullable TransactionStatus status) {
                Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(airlinesTransactionDto.getId());
                if (fetchAirlinesTransaction.isPresent()) {
                    AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
                    switch (airlinesTransactionDto.getPaymentStatus()) {
                        case WAITING_PAYMENT ->
                                paymentAirlinesTransactionService.paymentHasSelected(airlinesTransaction.getId());
                        case PAYMENT_EXPIRED -> {
                            StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = paymentAirlinesTransactionService.paymentHasExpired(airlinesTransaction.getId());
                            if (sm.getState().getId().equals(PaymentStatusEnum.PAYMENT_EXPIRED)) {
                                orderAirlinesTransactionService.orderHasExpired(airlinesTransaction.getId());
                            }
                        }
                        case PAID -> {
                            StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = paymentAirlinesTransactionService.paymentHasPaid(airlinesTransaction.getId());
                            if (sm.getState().getId().equals(PaymentStatusEnum.PAID)) {
                                StateMachine<OrderStatusEnum, OrderStatusEvent> orderStateMachine = orderAirlinesTransactionService.processIssued(airlinesTransaction.getId());
                                if (orderStateMachine.getState().getId().equals(OrderStatusEnum.PROCESS_ISSUED)) {
                                    try {
                                        airlinesService.issued(airlinesTransaction);
                                    } catch (JsonProcessingException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
