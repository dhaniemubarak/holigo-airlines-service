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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
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
        log.info("updateTransaction is running...");
        Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(id);
        if (fetchAirlinesTransaction.isEmpty()) {
            log.info("transaction is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
        StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.orderHasCanceled(airlinesTransaction.getId());
        log.info("sm -> {}", sm.getState().getId());
        if (sm.getState().getId().equals(OrderStatusEnum.ORDER_CANCELED)) {
            log.info("ORDER CANCELED");
            paymentAirlinesTransactionService.paymentHasCanceled(airlinesTransaction.getId());
            try {
                log.info("Try cancel book");
                airlinesService.cancelBook(airlinesTransaction);
            } catch (JsonProcessingException e) {
                log.info("Exception");
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


//    @GetMapping("/api/v1/airlines/transactions/{id}/issued")
//    public ResponseEntity<AirlinesTransactionDtoForUser> issuedTransaction(@PathVariable("id") Long id) {
//        Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(id);
//        if (fetchAirlinesTransaction.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
//        try {
//            airlinesService.issued(airlinesTransaction);
//        } catch (JsonLopProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return new ResponseEntity<>(airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDtoForUser(airlinesTransaction), HttpStatus.OK);
//    }

}
