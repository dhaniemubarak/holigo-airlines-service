package id.holigo.services.holigoairlinesservice.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.services.AirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
public class AirlinesBookController {

    private AirlinesTransactionService airlinesTransactionService;
    private AirlinesService airlinesService;

    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    private final static String PATH = "/api/v1/airlines/book";

    private final static String TRANSACTION_PATH = "/api/v1/transactions/{id}";

    @Autowired
    public void setAirlinesTransactionService(AirlinesTransactionService airlinesTransactionService) {
        this.airlinesTransactionService = airlinesTransactionService;
    }

    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> createBook(@RequestBody AirlinesBookDto airlinesBookDto,
                                                 @RequestHeader("user-id") Long userId) {
        TransactionDto transactionDto = airlinesTransactionService.createTransaction(airlinesBookDto, userId);
        try {
            AirlinesTransaction airlinesTransaction = airlinesService.createBook(Long.valueOf(transactionDto.getTransactionId()));
            if (!airlinesTransaction.getOrderStatus().equals(OrderStatusEnum.BOOKED)) {

                throw new BookException("Gagal booking, Silahkan pilih kembali penerbangan Anda", null, false, false);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(TRANSACTION_PATH)
                .buildAndExpand(transactionDto.getId().toString()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

}
