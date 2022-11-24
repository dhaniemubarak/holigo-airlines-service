package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.events.PaymentStatusEvent;
import org.springframework.statemachine.StateMachine;

public interface PaymentAirlinesTransactionService {


    void paymentHasSelected(Long id);
    StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasPaid(Long id);

    void paymentHasCanceled(Long id);

    StateMachine<PaymentStatusEnum, PaymentStatusEvent> paymentHasExpired(Long id);
}
