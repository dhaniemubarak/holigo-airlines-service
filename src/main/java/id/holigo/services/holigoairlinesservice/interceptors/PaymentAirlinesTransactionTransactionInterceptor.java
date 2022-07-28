package id.holigo.services.holigoairlinesservice.interceptors;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.PaymentStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.services.PaymentAirlinesTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PaymentAirlinesTransactionTransactionInterceptor extends StateMachineInterceptorAdapter<PaymentStatusEnum, PaymentStatusEvent> {

    private final AirlinesTransactionRepository airlinesTransactionRepository;

    @Override
    public void preStateChange(State<PaymentStatusEnum, PaymentStatusEvent> state, Message<PaymentStatusEvent> message,
                               Transition<PaymentStatusEnum, PaymentStatusEvent> transition,
                               StateMachine<PaymentStatusEnum, PaymentStatusEvent> stateMachine) {
        Optional.ofNullable(message).flatMap(msg -> Optional.of(Long.parseLong(
                Objects.requireNonNull(msg.getHeaders().get(PaymentAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER)).toString())
        )).ifPresent(id -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(id);
            airlinesTransaction.setPaymentStatus(state.getId());
            airlinesTransactionRepository.save(airlinesTransaction);
        });
    }
}
