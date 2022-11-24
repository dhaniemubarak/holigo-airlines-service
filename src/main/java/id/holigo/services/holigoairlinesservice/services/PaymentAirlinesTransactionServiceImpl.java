package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.PaymentStatusEvent;
import id.holigo.services.holigoairlinesservice.interceptors.PaymentAirlinesTransactionTransactionInterceptor;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentAirlinesTransactionServiceImpl implements PaymentAirlinesTransactionService {

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private final StateMachineFactory<PaymentStatusEnum, PaymentStatusEvent> stateMachineFactory;

    private final PaymentAirlinesTransactionTransactionInterceptor paymentAirlinesTransactionTransactionInterceptor;

    public static final String AIRLINES_TRANSACTION_HEADER = "airlines-transaction-id";

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Override
    public void paymentHasSelected(Long id) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(id);
        sendEvent(id, sm, PaymentStatusEvent.PAYMENT_SELECTED);
    }

    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasPaid(Long id) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(id);
        sendEvent(id, sm, PaymentStatusEvent.PAYMENT_PAID);
        return sm;
    }

    @Override
    public void paymentHasCanceled(Long id) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(id);
        sendEvent(id, sm, PaymentStatusEvent.PAYMENT_CANCEL);

    }

    @Override
    public StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasExpired(Long id) {
        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = build(id);
        sendEvent(id, sm, PaymentStatusEvent.PAYMENT_EXPIRED);
        return sm;
    }

    private void sendEvent(Long id, StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm,
                           PaymentStatusEvent event) {
        Message<PaymentStatusEvent> message = MessageBuilder.withPayload(event)
                .setHeader(AIRLINES_TRANSACTION_HEADER, id).build();
        sm.sendEvent(message);
    }

    private StateMachine<PaymentStatusEnum, PaymentStatusEvent> build(Long id) {
        AirlinesTransaction transaction = airlinesTransactionRepository.getById(id);

        StateMachine<PaymentStatusEnum, PaymentStatusEvent> sm = stateMachineFactory
                .getStateMachine(transaction.getId().toString());

        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(paymentAirlinesTransactionTransactionInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(
                    transaction.getPaymentStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
