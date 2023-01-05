package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import org.springframework.statemachine.StateMachine;

public interface OrderAirlinesTransactionService {

    void booked(Long id);

    void bookFailed(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> processIssued(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> waitingIssued(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> issued(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> issuedFailed(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasExpired(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasCanceled(Long id);
}
