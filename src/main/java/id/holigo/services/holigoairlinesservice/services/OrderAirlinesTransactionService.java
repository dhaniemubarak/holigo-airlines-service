package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import org.springframework.statemachine.StateMachine;

public interface OrderAirlinesTransactionService {

    StateMachine<OrderStatusEnum, OrderStatusEvent> booked(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> bookFailed(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasExpired(Long id);

    StateMachine<OrderStatusEnum, OrderStatusEvent> orderHasCanceled(Long id);
}
