package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionTripPassengerRepository;
import id.holigo.services.holigoairlinesservice.services.logs.LogService;
import id.holigo.services.holigoairlinesservice.services.logs.LogServiceImpl;
import id.holigo.services.holigoairlinesservice.web.mappers.PassengerMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.jms.Message;
import java.util.Map;


@Slf4j
@Service
public class RetrossAirlinesServiceImpl implements RetrossAirlinesService {


    @Value("${retross.credential.mmid}")
    private String RETROSS_ID;

    @Value("${retross.credential.rqid}")
    private String RETROSS_PASSKEY;

    private RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient;

    private LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    @Autowired
    public void setRetrossAirlinesServiceFeignClient(RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient) {
        this.retrossAirlinesServiceFeignClient = retrossAirlinesServiceFeignClient;
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
        log.info("Request body -> {}", objectMapper.writeValueAsString(requestScheduleDto));
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
        log.info("Response body -> {}", responseEntity.getBody());
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
                .build();
        log.info("Request body -> {}", objectMapper.writeValueAsString(requestFareDto));
        if (tripDto.getInquiry().getAirlinesCode().equals("IA")) {
            requestFareDto.setCabin(tripDto.getInquiry().getSeatClass().equals("E") ? "economy" : "business");
            responseEntity = retrossAirlinesServiceFeignClient.getInternationalFare(requestFareDto);
        } else {
            responseEntity = retrossAirlinesServiceFeignClient.getFare(requestFareDto);
        }
        log.info("Response body -> {}", responseEntity.getBody());
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
                .build();
        log.info("Request body -> {}", objectMapper.writeValueAsString(requestBookDto));
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.book(objectMapper.writeValueAsString(requestBookDto.build()));
        log.info("Response body -> {}", responseEntity.getBody());
        requestBookDto.setMmid("holivers");
        requestBookDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestBookDto));
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
                .build();
        log.info("Request body -> {}", objectMapper.writeValueAsString(requestCancelDto));
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.cancel(objectMapper.writeValueAsString(requestCancelDto));
        log.info("Request body -> {}", responseEntity.getBody());
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
                .build();
        log.info("Request body -> {}", objectMapper.writeValueAsString(requestIssuedDto));
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.cancel(objectMapper.writeValueAsString(requestIssuedDto));
        requestIssuedDto.setMmid("holivers");
        requestIssuedDto.setRqid("HOLI**********************GO");
        supplierLogDto.setLogRequest(objectMapper.writeValueAsString(requestIssuedDto));
        supplierLogDto.setLogResponse(responseEntity.getBody());
        logService.sendSupplierLog(supplierLogDto);
        log.info(responseEntity.getBody());
        log.info("Response body -> {}", objectMapper.writeValueAsString(responseEntity.getBody()));
        objectMapper.readValue(responseEntity.getBody(), ResponseCancelDto.class);
    }
}
