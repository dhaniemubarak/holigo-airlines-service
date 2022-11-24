package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.PaymentAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesTransactionMapper;
import id.holigo.services.common.model.AirlinesTransactionDtoForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AirlinesTransactionController {

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesService airlinesService;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    private PaymentAirlinesTransactionService paymentAirlinesTransactionService;

    private OrderAirlinesTransactionService orderAirlinesTransactionService;

    @Autowired
    public void setPaymentAirlinesTransactionService(PaymentAirlinesTransactionService paymentAirlinesTransactionService) {
        this.paymentAirlinesTransactionService = paymentAirlinesTransactionService;
    }

    @Autowired
    public void setOrderAirlinesTransactionService(OrderAirlinesTransactionService orderAirlinesTransactionService) {
        this.orderAirlinesTransactionService = orderAirlinesTransactionService;
    }

    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Autowired
    public void setAirlinesTransactionMapper(AirlinesTransactionMapper airlinesTransactionMapper) {
        this.airlinesTransactionMapper = airlinesTransactionMapper;
    }

    @PutMapping("/api/v1/airlines/transactions/{id}")
    public ResponseEntity<AirlinesTransactionDtoForUser> updateTransaction(@PathVariable("id") Long id) {
        Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(id);
        if (fetchAirlinesTransaction.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.orderHasCanceled(airlinesTransaction.getId());
        if (sm.getState().getId().equals(OrderStatusEnum.ORDER_CANCELED)) {
            paymentAirlinesTransactionService.paymentHasCanceled(airlinesTransaction.getId());
            try {
                airlinesService.cancelBook(airlinesTransaction);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return new ResponseEntity<>(airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDtoForUser(fetchAirlinesTransaction.get()), HttpStatus.OK);
    }

    @GetMapping("/api/v1/airlines/transactions/{id}")
    public ResponseEntity<AirlinesTransactionDtoForUser> getTransaction(@PathVariable("id") Long id) {
        Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(id);
        if (fetchAirlinesTransaction.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
        return new ResponseEntity<>(airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDtoForUser(airlinesTransaction), HttpStatus.OK);
    }


}
