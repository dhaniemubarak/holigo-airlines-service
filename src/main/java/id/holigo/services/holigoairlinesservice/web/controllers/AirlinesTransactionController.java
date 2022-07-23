package id.holigo.services.holigoairlinesservice.web.controllers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesTransactionMapper;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDtoForUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class AirlinesTransactionController {

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Autowired
    public void setAirlinesTransactionMapper(AirlinesTransactionMapper airlinesTransactionMapper) {
        this.airlinesTransactionMapper = airlinesTransactionMapper;
    }

    @GetMapping("/api/v1/airlines/transactions/{id}")
    public ResponseEntity<AirlinesTransactionDtoForUser> getTransaction(@PathVariable("id") Long id) {
        Optional<AirlinesTransaction> fetchAirlinesTransaction = airlinesTransactionRepository.findById(id);
        if (fetchAirlinesTransaction.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AirlinesTransaction airlinesTransaction = fetchAirlinesTransaction.get();
        log.info(airlinesTransaction.getId().toString());

        return new ResponseEntity<>(airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDtoForUser(airlinesTransaction), HttpStatus.OK);
    }
}
