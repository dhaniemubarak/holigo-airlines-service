package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesFinalFareRepository;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesTransactionRepository;
import id.holigo.services.holigoairlinesservice.repositories.ContactPersonRepository;
//import id.holigo.services.holigoairlinesservice.services.fare.FareService;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.exceptions.NotFoundException;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesAvailabilityMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesFareMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.AirlinesTransactionMapper;
import id.holigo.services.holigoairlinesservice.web.mappers.ContactPersonMapper;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AirlinesServiceImpl implements AirlinesService {

    private RetrossAirlinesService retrossAirlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private AirlinesFinalFareRepository airlinesFinalFareRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesFareMapper airlinesFareMapper;

//    private FareService fareService;

    private ContactPersonRepository contactPersonRepository;

    private ContactPersonMapper contactPersonMapper;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    private AirlinesTransactionRepository airlinesTransactionRepository;


    @Autowired
    public void setRetrossAirlinesService(RetrossAirlinesService retrossAirlinesService) {
        this.retrossAirlinesService = retrossAirlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityRepository(AirlinesAvailabilityRepository airlinesAvailabilityRepository) {
        this.airlinesAvailabilityRepository = airlinesAvailabilityRepository;
    }

    @Autowired
    public void setAirlinesFareMapper(AirlinesFareMapper airlinesFareMapper) {
        this.airlinesFareMapper = airlinesFareMapper;
    }

    @Autowired
    public void setAirlinesFinalFareRepository(AirlinesFinalFareRepository airlinesFinalFareRepository) {
        this.airlinesFinalFareRepository = airlinesFinalFareRepository;
    }


    @Autowired
    public void setAirlinesAvailabilityMapper(
            AirlinesAvailabilityMapper airlinesAvailabilityMapper) {
        this.airlinesAvailabilityMapper = airlinesAvailabilityMapper;
    }

    @Autowired
    public void setContactPersonRepository(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Autowired
    public void setContactPersonMapper(ContactPersonMapper contactPersonMapper) {
        this.contactPersonMapper = contactPersonMapper;
    }

    @Autowired
    public void setAirlinesTransactionMapper(AirlinesTransactionMapper airlinesTransactionMapper) {
        this.airlinesTransactionMapper = airlinesTransactionMapper;
    }

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Override
    public ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException {
        RequestScheduleDto requestScheduleDto = RequestScheduleDto.builder()
                .ac(inquiryDto.getAirlinesCode())
                .org(inquiryDto.getOriginAirportId())
                .des(inquiryDto.getDestinationAirportId())
                .tgl_dep(inquiryDto.getDepartureDate().toString())
                .tgl_ret(inquiryDto.getReturnDate() != null ? inquiryDto.getReturnDate().toString() : null)
                .flight(inquiryDto.getTripType())
                .adt(inquiryDto.getAdultAmount())
                .chd(inquiryDto.getChildAmount())
                .inf(inquiryDto.getInfantAmount())
                .cabin(inquiryDto.getSeatClass()).build();
        ResponseScheduleDto responseScheduleDto = retrossAirlinesService.getSchedule(requestScheduleDto);
        if (responseScheduleDto.getSchedule() == null) {
            return null;
        }
        return airlinesAvailabilityMapper.responseScheduleDtoToListAvailabilityDto(responseScheduleDto);
    }

    @Override
    public AirlinesFinalFareDto createFinalFares(TripDto tripDto, Long userId) throws JsonProcessingException {

        ResponseFareDto responseFareDto = retrossAirlinesService.getFare(tripDto);

        if (!responseFareDto.getError_code().equals("000")) {
            return null;
        }
        AirlinesFinalFare airlinesFinalFare = airlinesFareMapper.responseFareDtoToAirlinesFinalFare(responseFareDto,tripDto, userId);
        airlinesFinalFare.setIsIdentityNumberRequired(isIdentityNumberRequired(tripDto));
        return airlinesFareMapper.airlinesFinalFareToAirlinesFinalFareDto(airlinesFinalFare);
    }

    @Transactional
    @Override
    public AirlinesTransactionDto createTransaction(AirlinesBookDto airlinesBookDto, Long userId) {
        // FInd fare id
        AirlinesFinalFare airlinesFinalFare;
        Optional<AirlinesFinalFare> fetchFinalFare = airlinesFinalFareRepository.findById(airlinesBookDto.getFareId());
        if (fetchFinalFare.isPresent()) {
            airlinesFinalFare = fetchFinalFare.get();
        } else {
            throw new NotFoundException("Flight not available");
        }

        // create contact person
        ContactPerson savedContactPerson = contactPersonRepository
                .save(contactPersonMapper.contactPersonDtoToContactPerson(
                        airlinesBookDto.getContactPerson()));

        // Create airlines transaction
        AirlinesTransaction airlinesTransaction = airlinesFareMapper.airlinesFinalFareToAirlinesTransaction(airlinesFinalFare);
        airlinesTransaction.setContactPerson(savedContactPerson);

        // SET EXPIRED
        airlinesTransaction.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30L)));
        airlinesTransaction.setDiscountAmount(BigDecimal.valueOf(0.00));
        airlinesTransaction.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
        AirlinesTransaction savedAirlinesTransaction = airlinesTransactionRepository.save(airlinesTransaction);

        // create trip

        // create itinerary

        // create passengers

        // create transaction in transaction service

        return airlinesTransactionMapper.airlinesTransactionToAirlinesTransactionDto(savedAirlinesTransaction);
    }

    @Override
    public void saveAvailabilities(ListAvailabilityDto listAvailabilityDto) {

        List<AirlinesAvailability> airlinesAvailabilities = listAvailabilityDto.getDepartures().stream().map(airlinesAvailabilityMapper::airlinesAvailabilityDtoToAirlinesAvailability).toList();

        airlinesAvailabilityRepository.saveAll(airlinesAvailabilities);
    }


    private Boolean isIdentityNumberRequired(TripDto tripDto) {
        return switch (tripDto.getInquiry().getAirlinesCode()) {
            case "JT", "SJ" -> true;
            default -> false;
        };
    }
}
