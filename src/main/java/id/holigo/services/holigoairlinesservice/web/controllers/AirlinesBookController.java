package id.holigo.services.holigoairlinesservice.web.controllers;

import id.holigo.services.holigoairlinesservice.services.AirlinesService;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class AirlinesBookController {

    private AirlinesService airlinesService;

    private final static String PATH = "/api/v1/airlines/book";

    private final static String TRANSACTION_PATH = "/api/v1/transactions/{id}";

    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    @PostMapping(PATH)
    public ResponseEntity<HttpStatus> createBook(@RequestBody AirlinesBookDto airlinesBookDto,
                                                 @RequestHeader("user-id") Long userId) {
        AirlinesTransactionDto airlinesTransactionDto = airlinesService.createTransaction(airlinesBookDto, userId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(UriComponentsBuilder.fromPath(TRANSACTION_PATH)
                .buildAndExpand(airlinesTransactionDto.getId().toString()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

}
