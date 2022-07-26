package id.holigo.services.holigoairlinesservice.web.mappers;

import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.common.model.UserDtoForUser;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTrip;
import id.holigo.services.holigoairlinesservice.services.user.UserService;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDto;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionDtoForUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public abstract class AirlinesTransactionMapperDecorator implements AirlinesTransactionMapper {
    @Value("${airlines.iconUrl}")
    private String iconUrl;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    private AirlinesTransactionTripMapper airlinesTransactionTripMapper;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAirlinesTransactionMapper(AirlinesTransactionMapper airlinesTransactionMapper) {
        this.airlinesTransactionMapper = airlinesTransactionMapper;
    }

    @Autowired
    public void setAirlinesTransactionTripMapper(AirlinesTransactionTripMapper airlinesTransactionTripMapper) {
        this.airlinesTransactionTripMapper = airlinesTransactionTripMapper;
    }

    @Override
    public AirlinesTransactionDto airlinesTransactionToAirlinesTransactionDto(AirlinesTransaction airlinesTransaction) {
        return this.airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDto(airlinesTransaction);
    }

    @Override
    public AirlinesTransactionDtoForUser airlinesTransactionToAirlinesTransactionDtoForUser(AirlinesTransaction airlinesTransaction) {
        AirlinesTransactionDtoForUser airlinesTransactionDtoForUser = this.airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDtoForUser(airlinesTransaction);
        airlinesTransactionDtoForUser.setIconUrl(iconUrl);
        airlinesTransactionDtoForUser.setTrips(airlinesTransaction.getTrips().stream().map(airlinesTransactionTripMapper::airlinesTransactionTripToAirlinesTransactionTripDtoForUser).collect(Collectors.toList()));
        return airlinesTransactionDtoForUser;
    }

    @Override
    public TransactionDto airlinesTransactionToTransactionDto(AirlinesTransaction airlinesTransaction) {
        TransactionDto transactionDto = this.airlinesTransactionMapper.airlinesTransactionToTransactionDto(airlinesTransaction);
        UserDtoForUser userDtoForUser = userService.getUser(airlinesTransaction.getUserId());
        AirlinesTransactionTrip airlinesTransactionTrip = airlinesTransaction.getTrips().get(0);
        transactionDto.setTransactionType("AIR");
        transactionDto.setServiceId(1);
        transactionDto.setProductId(1);
        transactionDto.setPointAmount(BigDecimal.valueOf(0.0));
        transactionDto.setTransactionId(airlinesTransaction.getId().toString());
        transactionDto.setIndexProduct(airlinesTransactionTrip.getFlightNumber() + "|"
                + airlinesTransactionTrip.getOriginAirport().getId() + " - " + airlinesTransactionTrip.getDestinationAirport().getId() + "|"
                + airlinesTransactionTrip.getDepartureDate().toString() + " " + airlinesTransactionTrip.getDepartureTime().toString() + "|");
        transactionDto.setIndexUser(userDtoForUser.getName() + "|" + userDtoForUser.getPhoneNumber() + "|" + userDtoForUser.getEmail());
        return transactionDto;
    }
}
