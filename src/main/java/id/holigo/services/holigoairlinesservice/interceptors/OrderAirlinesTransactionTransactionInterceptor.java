package id.holigo.services.holigoairlinesservice.interceptors;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesTransactionServiceImpl;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderAirlinesTransactionTransactionInterceptor
        extends StateMachineInterceptorAdapter<OrderStatusEnum, OrderStatusEvent> {

    private final AirlinesTransactionRepository airlinesTransactionRepository;

    @Override
    public void preStateChange(State<OrderStatusEnum, OrderStatusEvent> state, Message<OrderStatusEvent> message,
                               Transition<OrderStatusEnum, OrderStatusEvent> transition,
                               StateMachine<OrderStatusEnum, OrderStatusEvent> stateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.of(
                Long.parseLong(Objects.requireNonNull(msg.getHeaders().get(OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER)).toString()))).ifPresent(id -> {
            log.info("OrderStatusTransactionInterceptor is running ...");
            log.info("with state id : {}", state.getId());
            AirlinesTransaction transaction = airlinesTransactionRepository.getById(id);
            transaction.setOrderStatus(state.getId());
            airlinesTransactionRepository.save(transaction);
        });
    }

}
