package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.interceptors.OrderAirlinesTransactionTransactionInterceptor;
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
public class OrderAirlinesTransactionServiceImpl implements OrderAirlinesTransactionService {

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private final StateMachineFactory<OrderStatusEnum, OrderStatusEvent> stateMachineFactory;

    private final OrderAirlinesTransactionTransactionInterceptor orderAirlinesTransactionTransactionInterceptor;
    public static final String AIRLINES_TRANSACTION_HEADER = "airlines-transaction-id";

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> booked(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.BOOK_SUCCESS);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> bookFailed(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.BOOK_FAIL);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> processIssued(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.PROCESS_ISSUED);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> issued(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.ISSUED_SUCCESS);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasExpired(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.ORDER_EXPIRE);
        return sm;
    }

    @Override
    public StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasCanceled(Long id) {
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = build(id);
        sendEvent(id, sm, OrderStatusEvent.ORDER_CANCEL);
        return sm;
    }

    private void sendEvent(Long id, StateMachine<OrderStatusEnum, OrderStatusEvent> sm,
                           OrderStatusEvent event) {
        Message<OrderStatusEvent> message = MessageBuilder.withPayload(event)
                .setHeader(AIRLINES_TRANSACTION_HEADER, id).build();
        sm.sendEvent(message);
    }

    private StateMachine<OrderStatusEnum, OrderStatusEvent> build(Long id) {
        AirlinesTransaction transaction = airlinesTransactionRepository.getById(id);

        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = stateMachineFactory
                .getStateMachine(transaction.getId().toString());

        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.addStateMachineInterceptor(orderAirlinesTransactionTransactionInterceptor);
            sma.resetStateMachine(new DefaultStateMachineContext<>(
                    transaction.getOrderStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}
