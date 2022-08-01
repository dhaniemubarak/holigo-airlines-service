package id.holigo.services.holigoairlinesservice.services.retross;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransaction;
import id.holigo.services.holigoairlinesservice.domain.AirlinesTransactionTripPassenger;
import id.holigo.services.holigoairlinesservice.domain.Passenger;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionTripPassengerRepository;
import id.holigo.services.holigoairlinesservice.web.mappers.PassengerMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class RetrossAirlinesServiceImpl implements RetrossAirlinesService {


    @Value("${retross.credential.mmid}")
    private String RETROSS_ID;

    @Value("${retross.credential.rqid}")
    private String RETROSS_PASSKEY;

    private RetrossAirlinesServiceFeignClient retrossAirlinesServiceFeignClient;

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
    public ResponseScheduleDto getSchedule(RequestScheduleDto requestScheduleDto) throws JsonProcessingException {
        requestScheduleDto.setMmid(RETROSS_ID);
        requestScheduleDto.setRqid(RETROSS_PASSKEY);
        requestScheduleDto.setApp("information");
        requestScheduleDto.setAction("get_schedule");
        log.info("Calling get schedule with request -> {}", objectMapper.writeValueAsString(requestScheduleDto));
//
        ResponseScheduleDto responseScheduleDto;
        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.getSchedule(requestScheduleDto);
//
        responseScheduleDto = objectMapper.readValue(responseEntity.getBody(), ResponseScheduleDto.class);

        log.info("ResponseEntity body -> {}", responseEntity.getBody());

        return responseScheduleDto;
    }

    @Override
    public ResponseFareDto getFare(TripDto tripDto, Map<String, String> roundTrip) throws JsonProcessingException {
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

        log.info("Calling get fare with request -> {}", objectMapper.writeValueAsString(requestFareDto));

        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.getFare(requestFareDto);

        log.info("ResponseEntity body -> {}", responseEntity.getBody());
//
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
        requestBookDto.setOrg(airlinesTransaction.getTrips().get(0).getOriginAirport().getId());
        requestBookDto.setDes(airlinesTransaction.getTrips().get(0).getDestinationAirport().getId());
        requestBookDto.setTrip(airlinesTransaction.getTripType().toString());
        requestBookDto.setAdt(airlinesTransaction.getTrips().get(0).getAdultAmount());
        requestBookDto.setChd(airlinesTransaction.getTrips().get(0).getChildAmount());
        requestBookDto.setInf(airlinesTransaction.getTrips().get(0).getInfantAmount());
        requestBookDto.setTgl_dep(airlinesTransaction.getTrips().get(0).getDepartureDate().toString());
        requestBookDto.setSelectedIdDep(airlinesTransaction.getTrips().get(0).getSupplierId());
        requestBookDto.setPassengers(airlinesTransactionTripPassengerRepository.findAllByTripId(airlinesTransaction.getTrips().get(0).getId()).stream().map(passengerMapper::airlinesTransactionTripPassengerToPassengerDto).toList());
        if (airlinesTransaction.getTripType().equals(TripType.R)) {
            requestBookDto.setSelectedIdRet(airlinesTransaction.getTrips().get(1).getSupplierId());
            requestBookDto.setTgl_ret(airlinesTransaction.getTrips().get(1).getDepartureDate().toString());
        }
        log.info("Format request -> {}", requestBookDto.build().toString());


        ResponseBookDto responseBookDto;
//        ResponseEntity<String> responseEntity = retrossAirlinesServiceFeignClient.book(requestBookDto.build().toString());
//        responseBookDto = objectMapper.readValue(responseEntity.getBody(), ResponseBookDto.class);
        String dummy = "{\"error_code\":\"001\",\"error_msg\":\"Nama penumpang tidak boleh 1 huruf\",\"notrx\":\"AIR220730887158\",\"mmid\":\"mastersip\",\"acDep\":\"JT\",\"Timelimit\":\"2022-07-30 08:06:00\",\"TotalAmount\":\"1735720\",\"NTA\":\"1714020\",\"PNRDep\":\"CZUTAB\"}";
        responseBookDto = objectMapper.readValue(dummy, ResponseBookDto.class);
        return responseBookDto;
    }
}
