package id.holigo.services.holigoairlinesservice.config;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.PaymentStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionTripRepository;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionServiceImpl;
import id.holigo.services.holigoairlinesservice.services.PaymentAirlinesTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Slf4j
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "paymentAirlinesTransactionSMF")
@Configuration
public class PaymentAirlinesTransactionSMConfig extends StateMachineConfigurerAdapter<PaymentStatusEnum, PaymentStatusEvent> {

    private final AirlinesTransactionRepository airlinesTransactionRepository;

    private final AirlinesTransactionTripRepository airlinesTransactionTripRepository;


    @Override
    public void configure(StateMachineStateConfigurer<PaymentStatusEnum, PaymentStatusEvent> states) throws Exception {
        states.withStates().initial(PaymentStatusEnum.SELECTING_PAYMENT)
                .states(EnumSet.allOf(PaymentStatusEnum.class))
                .end(PaymentStatusEnum.PAYMENT_FAILED)
                .end(PaymentStatusEnum.PAYMENT_CANCELED)
                .end(PaymentStatusEnum.PAYMENT_EXPIRED)
                .end(PaymentStatusEnum.REFUNDED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PaymentStatusEnum, PaymentStatusEvent> transitions)
            throws Exception {
        transitions.withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.WAITING_PAYMENT)
                .event(PaymentStatusEvent.PAYMENT_SELECTED).action(paymentHasSelected())
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAID)
                .event(PaymentStatusEvent.PAYMENT_PAID).action(paymentHasPaid())
                .and().withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.PAYMENT_EXPIRED)
                .event(PaymentStatusEvent.PAYMENT_EXPIRED).action(paymentHasExpired())
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAYMENT_EXPIRED)
                .event(PaymentStatusEvent.PAYMENT_EXPIRED).action(paymentHasExpired())
                .and().withExternal().source(PaymentStatusEnum.SELECTING_PAYMENT).target(PaymentStatusEnum.PAYMENT_CANCELED)
                .event(PaymentStatusEvent.PAYMENT_CANCEL).action(paymentHasCanceled())
                .and().withExternal().source(PaymentStatusEnum.WAITING_PAYMENT).target(PaymentStatusEnum.PAYMENT_CANCELED)
                .event(PaymentStatusEvent.PAYMENT_CANCEL).action(paymentHasCanceled());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<PaymentStatusEnum, PaymentStatusEvent> config)
            throws Exception {
        StateMachineListenerAdapter<PaymentStatusEnum, PaymentStatusEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<PaymentStatusEnum, PaymentStatusEvent> from,
                                     State<PaymentStatusEnum, PaymentStatusEvent> to) {
                log.info(String.format("stateChange(from: %s, to %s)", from.getId(), to.getId()));
            }
        };
        config.withConfiguration().listener(adapter);
    }

    @Bean
    public Action<PaymentStatusEnum, PaymentStatusEvent> paymentHasPaid() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(Long.parseLong(stateContext
                    .getMessageHeader(PaymentAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.PAID);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });

        };
    }

    @Bean
    public Action<PaymentStatusEnum, PaymentStatusEvent> paymentHasExpired() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(
                    Long.parseLong(stateContext.getMessageHeader(
                            OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.PAYMENT_EXPIRED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }

    @Bean
    public Action<PaymentStatusEnum, PaymentStatusEvent> paymentHasCanceled() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(
                    Long.parseLong(stateContext.getMessageHeader(
                            OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.PAYMENT_CANCELED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }

    @Bean
    public Action<PaymentStatusEnum, PaymentStatusEvent> paymentHasSelected() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(
                    Long.parseLong(stateContext.getMessageHeader(
                            OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.WAITING_PAYMENT);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }
}
