package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.*;
import id.holigo.services.holigoairlinesservice.config.KafkaTopicConfig;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripPassenger;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionTripPassengerRepository;
import id.holigo.services.holigoairlinesservice.services.OrderAirlinesTransactionService;
import id.holigo.services.holigoairlinesservice.services.logs.LogService;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import id.holigo.services.holigoairlinesservice.web.mappers.PassengerMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Service
@RequiredArgsConstructor
public class RetrossAirlinesServiceImpl implements RetrossAirlinesService {


    @Value("${retross.credential.mmid}")
    private String RETROSS_ID;

    @Value("${retross.credential.rqid}")
    private String RETROSS_PASSKEY;

    private RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient;

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private OrderAirlinesTransactionService orderAirlinesTransactionService;

    private final KafkaTemplate<String, AirlinesTransactionDtoForUser> airlinesKafkaTemplate;

    private TransactionService transactionService;

    private LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setRetrossAirlinesServiceFeignClient(RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient) {
        this.retrossAirlinesServiceFeignClient = retrossAirlinesServiceFeignClient;
    }

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Autowired
    public void setOrderAirlinesTransactionService(OrderAirlinesTransactionService orderAirlinesTransactionService) {
        this.orderAirlinesTransactionService = orderAirlinesTransactionService;
    }

    private ObjectMapper objectMapper;

    private PassengerMapper passengerMapper;

    private AirlinesTransactionTripPassengerRepository airlinesTransactionTripPassengerRepository;

    @Autowired
    public void setAirlinesTransactionTripPassengerRepository(AirlinesTransactionTripPassengerRepository airlinesTransactionTripPassengerRepository) {
        this.airlinesTransactionTripPassengerRepository = airlinesTransactionTripPassengerRepository;
    }

    @Autowired
    public void setPassengerMapper(PassengerMapper passengerMapper) {
        this.passengerMapper = passengerMapper;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto, Long userId) throws JsonProcessingException {
        requestScheduleDto.setMmid(RETROSS_ID);
        requestScheduleDto.setRqid(RETROSS_PASSKEY);
        requestScheduleDto.setApp("information");
        requestScheduleDto.setAction("get_schedule");
        ResponseScheduleDto responseScheduleDto;
        ResponseEntity<String> responseEntity;
        SupplierLogDto supplierLogDto = SupplierLogDto.builder()
                .userId(userId)
                .supplier("retross")
                .code("AIR")
                .build();
        if (requestScheduleDto.getAc().equals("IA")) {
            if (requestScheduleDto.getCabin().equals("E")) {
                requestScheduleDto.setCabin("economy");
            } else {
                requestScheduleDto.setCabin("business");
            }
            supplierLogDto.setUrl("http://ws.retross.com/airline/international/");
            responseEntity = retrossAirlinesServiceFeignClient.getInternationalSchedule(requestScheduleDto);
        } else {
            supplierLogDto.setUrl("http://ws.retross.com/airline/domestik/");
            responseEntity = retrossAirlinesServiceFeignClient.getSchedule(requestScheduleDto);

        }
        requestScheduleDto.setMmid("holivers");
        requestScheduleDto.setRqid("HOLI******************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestScheduleDto));
        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
        responseScheduleDto = objectMapper.readValue(responseEntity.getBody(), ResponseScheduleDto.class);
        return responseScheduleDto;
    }

    @Override
    public ResponseFareDto getFare(TripDto tripDto, Map<String, String> roundTrip, Long userId) throws JsonProcessingException {
        RequestFareDto requestFareDto = RequestFareDto.builder()
                .mmid(RETROSS_ID)
                .rqid(RETROSS_PASSKEY)
                .app("information")
                .action("get_fare")
                .acDep(tripDto.getInquiry().getAirlinesCode())
                .org(tripDto.getInquiry().getOriginAirport().getId())
                .des(tripDto.getInquiry().getDestinationAirport().getId())
                .tgl_dep(tripDto.getInquiry().getDepartureDate().toString())
                .tgl_ret(tripDto.getInquiry().getReturnDate() != null ? tripDto.getInquiry().getReturnDate().toString() : null)
                .flight(tripDto.getInquiry().getTripType().toString())
                .adt(tripDto.getInquiry().getAdultAmount())
                .chd(tripDto.getInquiry().getChildAmount())
                .inf(tripDto.getInquiry().getInfantAmount())
                .selectedIdDep(tripDto.getTrip().getFare().getSelectedId()).build();
        if (roundTrip.containsKey("selectedId") && roundTrip.containsKey("airlinesCode")) {
            requestFareDto.setSelectedIdRet(roundTrip.get("selectedId"));
            requestFareDto.setAcRet(roundTrip.get("airlinesCode"));
        }
        ResponseFareDto responseFareDto;
        ResponseEntity<String> responseEntity;
        SupplierLogDto supplierLogDto = SupplierLogDto.builder()
                .userId(userId)
                .supplier("retross")
                .code("AIR")
                .url("http://ws.retross.com/airline/domestik/")
                .build();
        if (tripDto.getInquiry().getAirlinesCode().equals("IA")) {
            supplierLogDto.setUrl("http://ws.retross.com/airline/international/");
            requestFareDto.setCabin(tripDto.getInquiry().getSeatClass().equals("E") ? "economy" : "business");
            responseEntity = retrossAirlinesServiceFeignClient.getInternationalFare(requestFareDto);
        } else {
            responseEntity = retrossAirlinesServiceFeignClient.getFare(requestFareDto);
        }
        requestFareDto.setMmid("holivers");
        requestFareDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestFareDto));
        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
        responseFareDto = objectMapper.readValue(responseEntity.getBody(), ResponseFareDto.class);
        return responseFareDto;
    }

    @Override
    public ResponseBookDto createBook(AirlinesTransaction airlinesTransaction) throws JsonProcessingException {
        RequestBookDto requestBookDto = new RequestBookDto();
        requestBookDto.setMmid(RETROSS_ID);
        requestBookDto.setRqid(RETROSS_PASSKEY);
        requestBookDto.setApp("transaction");
        requestBookDto.setAction("booking");
        requestBookDto.setCpname(airlinesTransaction.getContactPerson().getName());
        requestBookDto.setCpmail(airlinesTransaction.getContactPerson().getEmail());
        requestBookDto.setCptlp(airlinesTransaction.getContactPerson().getPhoneNumber());
        requestBookDto.setAcDep(airlinesTransaction.getTrips().get(0).getAirlinesCode());
        requestBookDto.setOrg(airlinesTransaction.getTrips().get(0).getOriginAirport().getId());
        requestBookDto.setDes(airlinesTransaction.getTrips().get(0).getDestinationAirport().getId());
        requestBookDto.setFlight(airlinesTransaction.getTripType().toString());
        requestBookDto.setAdt(airlinesTransaction.getTrips().get(0).getAdultAmount());
        requestBookDto.setChd(airlinesTransaction.getTrips().get(0).getChildAmount());
        requestBookDto.setInf(airlinesTransaction.getTrips().get(0).getInfantAmount());
        requestBookDto.setTgl_dep(airlinesTransaction.getTrips().get(0).getDepartureDate().toString());
        requestBookDto.setSelectedIdDep(airlinesTransaction.getTrips().get(0).getSupplierId());
        requestBookDto.setPassengers(airlinesTransactionTripPassengerRepository.findAllByTripId(airlinesTransaction.getTrips().get(0).getId()).stream().map(passengerMapper::airlinesTransactionTripPassengerToPassengerDto).toList());
        if (airlinesTransaction.getTripType().equals(TripType.R)) {
            requestBookDto.setAcRet(airlinesTransaction.getTrips().get(1).getAirlinesCode());
            requestBookDto.setSelectedIdRet(airlinesTransaction.getTrips().get(1).getSupplierId());
            requestBookDto.setTgl_ret(airlinesTransaction.getTrips().get(1).getDepartureDate().toString());
        }
        SupplierLogDto supplierLogDto = SupplierLogDto.builder()
                .userId(airlinesTransaction.getUserId())
                .supplier("retross")
                .code("AIR")
                .url("http://ws.retross.com/airline/domestik/")
                .build();
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.book(objectMapper.writeValueAsString(requestBookDto.build()));
        requestBookDto.setMmid("holivers");
        requestBookDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestBookDto.build()));
        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
        return objectMapper.readValue(responseEntity.getBody(), ResponseBookDto.class);
//        String dummy = "{\"error_code\":\"000\",\"error_msg\":\"\",\"notrx\":\"AIR220803247758\",\"mmid\":\"holigo\",\"acDep\":\"JT\",\"Timelimit\":\"2022-08-03 04:46:00\",\"TotalAmount\":\"4319480\",\"NTA\":\"4248780\",\"PNRDep\":\"EGREFV\"}";
//        responseBookDto = objectMapper.readValue(dummy, ResponseBookDto.class);
//        return responseBookDto;
    }

    @Override
    public void cancelBook(AirlinesTransaction airlinesTransaction) throws JsonProcessingException {
        RequestCancelDto requestCancelDto = RequestCancelDto.builder()
                .action("cancel")
                .app("transaction")
                .rqid(RETROSS_PASSKEY)
                .mmid(RETROSS_ID)
                .notrx(airlinesTransaction.getTrips().get(0).getSupplierTransactionId()).build();
        SupplierLogDto supplierLogDto = SupplierLogDto.builder()
                .userId(airlinesTransaction.getUserId())
                .supplier("retross")
                .code("AIR")
                .url("http://ws.retross.com/airline/domestik/")
                .build();
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.cancel(objectMapper.writeValueAsString(requestCancelDto));
        requestCancelDto.setMmid("holivers");
        requestCancelDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestCancelDto));
        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
        objectMapper.readValue(responseEntity.getBody(), ResponseCancelDto.class);
    }

    @Override
    public void issued(AirlinesTransaction airlinesTransaction) throws JsonProcessingException {
        RequestIssuedDto requestIssuedDto = RequestIssuedDto.builder()
                .action("issued")
                .app("transaction")
                .rqid(RETROSS_PASSKEY)
                .mmid(RETROSS_ID)
                .notrx(airlinesTransaction.getTrips().get(0).getSupplierTransactionId()).build();
        SupplierLogDto supplierLogDto = SupplierLogDto.builder()
                .userId(airlinesTransaction.getUserId())
                .supplier("retross")
                .code("AIR")
                .url("http://ws.retross.com/airline/domestik/")
                .build();
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.issued(objectMapper.writeValueAsString(requestIssuedDto));
        requestIssuedDto.setMmid("holivers");
        requestIssuedDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestIssuedDto));
//        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
//        log.info("Response body -> {}", objectMapper.writeValueAsString(responseEntity.getBody()));
        ResponseIssuedDto responseIssuedDto = objectMapper.readValue(responseEntity.getBody(), ResponseIssuedDto.class);
//        ResponseIssuedDto responseIssuedDto = objectMapper.readValue("{\"error_code\":\"000\",\"error_msg\":\"\",\"notrx\":\"AIR221120479282\",\"mmid\":\"mastersip\",\"status\":\"ISSUED\",\"acDep\":\"JT\",\"acRet\":\"JT\",\"PNRDep\":\"ZYPAID\",\"PNRRet\":\"ZYPAID\",\"TotalAmount\":\"10621440\",\"NTA\":\"10486200\",\"saldo\":\"-587920\",\"penumpang\":[{\"jns\":\"A\",\"title\":\"MR\",\"fn\":\"Mochamad\",\"ln\":\"Ramdhanie\",\"birth\":\"0000-00-00\",\"hp\":\"081347459776\",\"noticket\":\"9902146454424\",\"noticket_ret\":\"9902146454424\"},{\"jns\":\"A\",\"title\":\"MRS\",\"fn\":\"Anisa\",\"ln\":\"Nursantika\",\"birth\":\"0000-00-00\",\"hp\":\"085248790078\",\"noticket\":\"9902146454425\",\"noticket_ret\":\"9902146454425\"}]}", ResponseIssuedDto.class);
        if (responseIssuedDto.getError_code().equals("001")) {
            airlinesTransaction.setSupplierMessage(responseIssuedDto.getError_msg());
            airlinesTransactionRepository.save(airlinesTransaction);
            StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.issuedFailed(airlinesTransaction.getId());
            if (sm.getState().getId().equals(OrderStatusEnum.ISSUED_FAILED)) {
                airlinesKafkaTemplate.send(KafkaTopicConfig.UPDATE_ORDER_STATUS_AIRLINES_TRANSACTION, AirlinesTransactionDtoForUser.builder().id(airlinesTransaction.getId())
                        .orderStatus(OrderStatusEnum.ISSUED_FAILED).build());
            }
        }
        if (responseIssuedDto.getError_code().equals("000")) {
            if (responseIssuedDto.getStatus().equalsIgnoreCase("ISSUED")) {
                AtomicInteger indexTrip = new AtomicInteger();
                airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                    List<AirlinesTransactionTripPassenger> tripPassengers = airlinesTransactionTripPassengerRepository.findAllByTripId(airlinesTransactionTrip.getId());
                    AtomicInteger indexPassenger = new AtomicInteger();
                    for (AirlinesTransactionTripPassenger tripPassenger : tripPassengers) {
                        if (indexTrip.get() == 0) {
                            tripPassenger.setTicketNumber(responseIssuedDto.getPenumpang().get(indexPassenger.get()).getNoticket());
                        } else {
                            tripPassenger.setTicketNumber(responseIssuedDto.getPenumpang().get(indexPassenger.get()).getNoticket_ret());
                        }
                        airlinesTransactionTripPassengerRepository.save(tripPassenger);
                        indexPassenger.getAndIncrement();
                    }
                    indexTrip.getAndIncrement();
                });
                StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.issued(airlinesTransaction.getId());
                if (sm.getState().getId().equals(OrderStatusEnum.ISSUED)) {
                    transactionService.updateOrderStatusTransaction(TransactionDto.builder()
                            .orderStatus(OrderStatusEnum.ISSUED)
                            .id(airlinesTransaction.getTransactionId()).build());
                }
            }
        }
    }
}
