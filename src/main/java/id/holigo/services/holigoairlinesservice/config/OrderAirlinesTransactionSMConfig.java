package id.holigo.services.holigoairlinesservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionTripRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
@EnableStateMachineFactory(name = "orderAirlinesTransactionSMF")
public class OrderAirlinesTransactionSMConfig extends StateMachineConfigurerAdapter<OrderStatusEnum, OrderStatusEvent> {

    private final AirlinesTransactionRepository airlinesTransactionRepository;

    private final AirlinesTransactionTripRepository airlinesTransactionTripRepository;

    private final AirlinesService airlinesService;

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatusEnum, OrderStatusEvent> states) throws Exception {
        states.withStates().initial(OrderStatusEnum.PROCESS_BOOK)
                .states(EnumSet.allOf(OrderStatusEnum.class))
                .end(OrderStatusEnum.BOOK_FAILED)
                .end(OrderStatusEnum.ISSUED)
                .end(OrderStatusEnum.ISSUED_FAILED)
                .end(OrderStatusEnum.ORDER_CANCELED)
                .end(OrderStatusEnum.ORDER_EXPIRED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatusEnum, OrderStatusEvent> transitions)
            throws Exception {
        transitions.withExternal().source(OrderStatusEnum.PROCESS_BOOK).target(OrderStatusEnum.BOOKED)
                .event(OrderStatusEvent.BOOK_SUCCESS).action(bookSuccess())
                .and()
                .withExternal().source(OrderStatusEnum.PROCESS_BOOK).target(OrderStatusEnum.BOOK_FAILED)
                .event(OrderStatusEvent.BOOK_FAIL).action(bookFailed())
                .and()
                .withExternal().source(OrderStatusEnum.BOOKED).target(OrderStatusEnum.PROCESS_ISSUED)
                .event(OrderStatusEvent.PROCESS_ISSUED).action(processIssued())
                .and()
                .withExternal().source(OrderStatusEnum.PROCESS_ISSUED).target(OrderStatusEnum.ISSUED)
                .event(OrderStatusEvent.ISSUED_SUCCESS)
                .and()
                .withExternal().source(OrderStatusEnum.PROCESS_ISSUED).target(OrderStatusEnum.ISSUED_FAILED)
                .event(OrderStatusEvent.ISSUED_FAIL)
                .and()
                .withExternal().source(OrderStatusEnum.PROCESS_ISSUED).target(OrderStatusEnum.WAITING_ISSUED)
                .event(OrderStatusEvent.WAITING_ISSUED)
                .and()
                .withExternal().source(OrderStatusEnum.PROCESS_ISSUED).target(OrderStatusEnum.RETRYING_ISSUED)
                .event(OrderStatusEvent.RETRYING_ISSUED)
                .and()
                .withExternal().source(OrderStatusEnum.BOOKED).target(OrderStatusEnum.ORDER_CANCELED)
                .event(OrderStatusEvent.ORDER_CANCEL).action(orderCanceled())
                .and()
                .withExternal().source(OrderStatusEnum.BOOKED).target(OrderStatusEnum.ORDER_EXPIRED)
                .event(OrderStatusEvent.ORDER_EXPIRE);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<OrderStatusEnum, OrderStatusEvent> config)
            throws Exception {
        StateMachineListenerAdapter<OrderStatusEnum, OrderStatusEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<OrderStatusEnum, OrderStatusEvent> from,
                                     State<OrderStatusEnum, OrderStatusEvent> to) {
                log.info(String.format("stateChange(from: %s, to %s)", from.getId(), to.getId()));
            }
        };
        config.withConfiguration().listener(adapter);
    }

    @Bean
    public Action<OrderStatusEnum, OrderStatusEvent> bookSuccess() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository
                    .getById(Long.parseLong(stateContext
                            .getMessageHeader(OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            try {
                airlinesService.createBook(airlinesTransaction.getId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.BOOKED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }

    @Bean
    public Action<OrderStatusEnum, OrderStatusEvent> processIssued() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository
                    .getById(Long.parseLong(stateContext
                            .getMessageHeader(OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            try {
                airlinesService.issued(airlinesTransaction);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.PROCESS_ISSUED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }

    @Bean
    public Action<OrderStatusEnum, OrderStatusEvent> bookFailed() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository
                    .getById(Long.parseLong(stateContext
                            .getMessageHeader(OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.BOOK_FAILED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
        };
    }

    @Bean
    public Action<OrderStatusEnum, OrderStatusEvent> orderCanceled() {
        return stateContext -> {
            AirlinesTransaction airlinesTransaction = airlinesTransactionRepository
                    .getById(Long.parseLong(stateContext
                            .getMessageHeader(OrderAirlinesTransactionServiceImpl.AIRLINES_TRANSACTION_HEADER).toString()));
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.ORDER_CANCELED);
                airlinesTransactionTripRepository.save(airlinesTransactionTrip);
            });
            try {
                airlinesService.cancelBook(airlinesTransaction);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
