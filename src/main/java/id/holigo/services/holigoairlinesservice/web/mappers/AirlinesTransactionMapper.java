package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = ContactPersonMapper.class)
public interface AirlinesTransactionMapper {
    AirlinesTransactionDto airlinesTransactionToAirlinesTransactionDto(AirlinesTransaction airlinesTransaction);
}
