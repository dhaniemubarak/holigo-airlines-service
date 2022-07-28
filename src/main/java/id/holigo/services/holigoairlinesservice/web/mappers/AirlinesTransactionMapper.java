package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDto;
import id.holigo.services.common.model.AirlinesTransactionDtoForUser;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@DecoratedWith(AirlinesTransactionMapperDecorator.class)
@Mapper(uses = {ContactPersonMapper.class})
public interface AirlinesTransactionMapper {
    AirlinesTransactionDto airlinesTransactionToAirlinesTransactionDto(AirlinesTransaction airlinesTransaction);

    @Mapping(target = "iconUrl", ignore = true)
    AirlinesTransactionDtoForUser airlinesTransactionToAirlinesTransactionDtoForUser(AirlinesTransaction airlinesTransaction);

    @Mapping(target = "voucherCode", ignore = true)
    @Mapping(target = "transactionType", ignore = true)
    @Mapping(target = "serviceId", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "pointAmount", ignore = true)
    @Mapping(target = "paymentServiceId", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "invoiceNumber", ignore = true)
    @Mapping(target = "indexUser", ignore = true)
    @Mapping(target = "indexProduct", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "id", ignore = true)
    TransactionDto airlinesTransactionToTransactionDto(AirlinesTransaction airlinesTransaction);
}
