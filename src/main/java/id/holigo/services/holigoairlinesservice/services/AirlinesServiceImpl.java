package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.services.fare.FareService;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class AirlinesServiceImpl implements AirlinesService {

    private FareService fareService;

    private RetrossAirlinesService retrossAirlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private AirlinesFinalFareRepository airlinesFinalFareRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesFinalFareMapper airlinesFinalFareMapper;

//    private FareService fareService;

    private ContactPersonRepository contactPersonRepository;

    private ContactPersonMapper contactPersonMapper;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesFinalFareTripMapper airlinesFinalFareTripMapper;

    private AirlinesFinalFareTripRepository airlinesFinalFareTripRepository;

    private InquiryMapper inquiryMapper;

    @Autowired
    public void setRetrossAirlinesService(RetrossAirlinesService retrossAirlinesService) {
        this.retrossAirlinesService = retrossAirlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityRepository(AirlinesAvailabilityRepository airlinesAvailabilityRepository) {
        this.airlinesAvailabilityRepository = airlinesAvailabilityRepository;
    }

    @Autowired
    public void setAirlinesFareMapper(AirlinesFinalFareMapper airlinesFinalFareMapper) {
        this.airlinesFinalFareMapper = airlinesFinalFareMapper;
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

    @Autowired
    public void setAirlinesFinalFareTripMapper(AirlinesFinalFareTripMapper airlinesFinalFareTripMapper) {
        this.airlinesFinalFareTripMapper = airlinesFinalFareTripMapper;
    }

    @Autowired
    public void setAirlinesFinalFareTripRepository(AirlinesFinalFareTripRepository airlinesFinalFareTripRepository) {
        this.airlinesFinalFareTripRepository = airlinesFinalFareTripRepository;
    }

    @Autowired
    public void setInquiryMapper(InquiryMapper inquiryMapper) {
        this.inquiryMapper = inquiryMapper;
    }

    @Autowired
    public void setFareService(FareService fareService) {
        this.fareService = fareService;
    }

    @Override
    public ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException {
        RequestScheduleDto requestScheduleDto = RequestScheduleDto.builder()
                .ac(inquiryDto.getAirlinesCode())
                .org(inquiryDto.getOriginAirportId())
                .des(inquiryDto.getDestinationAirportId())
                .tgl_dep(inquiryDto.getDepartureDate().toString())
                .tgl_ret(inquiryDto.getReturnDate() != null ? inquiryDto.getReturnDate().toString() : null)
                .flight(inquiryDto.getTripType().toString())
                .adt(inquiryDto.getAdultAmount())
                .chd(inquiryDto.getChildAmount())
                .inf(inquiryDto.getInfantAmount())
                .cabin(inquiryDto.getSeatClass()).build();
        ResponseScheduleDto responseScheduleDto = retrossAirlinesService.getSchedule(requestScheduleDto);
        if (responseScheduleDto.getSchedule() == null) {
            return null;
        }
        return airlinesAvailabilityMapper.responseScheduleDtoToListAvailabilityDto(responseScheduleDto, inquiryDto.getUserId());
    }

    @Transactional
    @Override
    public AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId) {

        Set<AirlinesFinalFareTrip> airlinesFinalFareTrips = new HashSet<>();


        for (int i = 0; i < requestFinalFareDto.getTrips().size(); i++) {
            ResponseFareDto responseFareDto;
            TripDto tripDto = requestFinalFareDto.getTrips().get(i);
            AirlinesAvailability airlinesAvailability = airlinesAvailabilityRepository
                    .getAirlinesAvailabilityById(tripDto.getTrip().getId().toString());
            AirlinesFinalFareTrip airlinesFinalFareTrip = airlinesFinalFareTripMapper
                    .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability);
            airlinesFinalFareTrip.setAdultAmount(tripDto.getInquiry().getAdultAmount());
            airlinesFinalFareTrip.setChildAmount(tripDto.getInquiry().getChildAmount());
            airlinesFinalFareTrip.setInfantAmount(tripDto.getInquiry().getInfantAmount());
            if (tripDto.getInquiry().getTripType() != TripType.R && i != 1) {
                try {
                    responseFareDto = retrossAirlinesService.getFare(tripDto);
                    if (!responseFareDto.getError_code().equals("000")) {
                        throw new Exception("Failed get final fare from airlines");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                responseFareDto = ResponseFareDto.builder()
                        .totalAmount(BigDecimal.valueOf(0.00))
                        .ntaAmount(BigDecimal.valueOf(0.00))
                        .build();
            }
            airlinesFinalFareTrip.setIsPriceIncluded(responseFareDto.getTotalAmount().equals(BigDecimal.valueOf(0.00)));
            airlinesFinalFareTrip.setFareAmount(responseFareDto.getTotalAmount());
            airlinesFinalFareTrip.setNtaAmount(responseFareDto.getNtaAmount());
            airlinesFinalFareTrip.setNraAmount(airlinesFinalFareTrip.getFareAmount()
                    .subtract(airlinesFinalFareTrip.getNtaAmount()));


            FareDto fareDto = null;
            try {
                fareDto = fareService.getFareDetail(FareDetailDto.builder().ntaAmount(airlinesFinalFareTrip.getNtaAmount())
                        .nraAmount(airlinesFinalFareTrip.getNraAmount()).productId(1).userId(userId).build());
            } catch (JMSException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            airlinesFinalFareTrip.setAdminAmount(BigDecimal.valueOf(0.00));
            airlinesFinalFareTrip.setCpAmount(fareDto.getCpAmount());
            airlinesFinalFareTrip.setMpAmount(fareDto.getMpAmount());
            airlinesFinalFareTrip.setIpAmount(fareDto.getIpAmount());
            airlinesFinalFareTrip.setHpAmount(fareDto.getHpAmount());
            airlinesFinalFareTrip.setHvAmount(fareDto.getHvAmount());
            airlinesFinalFareTrip.setPrAmount(fareDto.getPrAmount());
            airlinesFinalFareTrip.setIpcAmount(fareDto.getIpcAmount());
            airlinesFinalFareTrip.setHpcAmount(fareDto.getHpcAmount());
            airlinesFinalFareTrip.setPrcAmount(fareDto.getPrcAmount());
            airlinesFinalFareTrip.setLossAmount(fareDto.getLossAmount());
            airlinesFinalFareTrips.add(airlinesFinalFareTrip);

        }

        AirlinesFinalFare airlinesFinalFare = AirlinesFinalFare.builder()
                .fareAmount(BigDecimal.valueOf(0.00))
                .adminAmount(BigDecimal.valueOf(0.00))
                .ntaAmount(BigDecimal.valueOf(0.00))
                .nraAmount(BigDecimal.valueOf(0.00))
                .cpAmount(BigDecimal.valueOf(0.00))
                .mpAmount(BigDecimal.valueOf(0.00))
                .ipAmount(BigDecimal.valueOf(0.00))
                .hpAmount(BigDecimal.valueOf(0.00))
                .hvAmount(BigDecimal.valueOf(0.00))
                .prAmount(BigDecimal.valueOf(0.00))
                .ipcAmount(BigDecimal.valueOf(0.00))
                .hpcAmount(BigDecimal.valueOf(0.00))
                .prcAmount(BigDecimal.valueOf(0.00))
                .lossAmount(BigDecimal.valueOf(0.00))
                .build();
        airlinesFinalFare.setUserId(userId);
        airlinesFinalFare.setIsBookable(true);
        airlinesFinalFareTrips.forEach(airlinesFinalFareTrip -> {
            airlinesFinalFare.setFareAmount(airlinesFinalFare.getFareAmount().add(airlinesFinalFareTrip.getFareAmount()));
            airlinesFinalFare.setAdminAmount(airlinesFinalFare.getAdminAmount().add(airlinesFinalFareTrip.getAdminAmount()));
            airlinesFinalFare.setNtaAmount(airlinesFinalFare.getNtaAmount().add(airlinesFinalFareTrip.getNtaAmount()));
            airlinesFinalFare.setNraAmount(airlinesFinalFare.getNraAmount().add(airlinesFinalFareTrip.getNraAmount()));
            airlinesFinalFare.setCpAmount(airlinesFinalFare.getCpAmount().add(airlinesFinalFareTrip.getCpAmount()));
            airlinesFinalFare.setMpAmount(airlinesFinalFare.getMpAmount().add(airlinesFinalFareTrip.getMpAmount()));
            airlinesFinalFare.setIpAmount(airlinesFinalFare.getIpAmount().add(airlinesFinalFareTrip.getIpAmount()));
            airlinesFinalFare.setHpAmount(airlinesFinalFare.getHpAmount().add(airlinesFinalFareTrip.getHpAmount()));
            airlinesFinalFare.setHvAmount(airlinesFinalFare.getHvAmount().add(airlinesFinalFareTrip.getHvAmount()));
            airlinesFinalFare.setPrAmount(airlinesFinalFare.getPrAmount().add(airlinesFinalFareTrip.getPrAmount()));
            airlinesFinalFare.setIpcAmount(airlinesFinalFare.getIpcAmount().add(airlinesFinalFareTrip.getIpcAmount()));
            airlinesFinalFare.setHpcAmount(airlinesFinalFare.getHpcAmount().add(airlinesFinalFareTrip.getHpcAmount()));
            airlinesFinalFare.setPrcAmount(airlinesFinalFare.getPrcAmount().add(airlinesFinalFareTrip.getPrcAmount()));
            airlinesFinalFare.setLossAmount(airlinesFinalFare.getLossAmount().add(airlinesFinalFareTrip.getLossAmount()));
        });
        AirlinesFinalFare savedAirlinesFinalFare = airlinesFinalFareRepository.save(airlinesFinalFare);
        airlinesFinalFareTrips.forEach(airlinesFinalFareTrip -> airlinesFinalFareTrip.setFinalFare(savedAirlinesFinalFare));

        airlinesFinalFareTripRepository.saveAll(airlinesFinalFareTrips);
        return savedAirlinesFinalFare;
    }

    @Transactional
    @Override
    public void saveAvailabilities(ListAvailabilityDto listAvailabilityDto) {

        List<AirlinesAvailability> airlinesAvailabilities = listAvailabilityDto.getDepartures().stream().map(airlinesAvailabilityMapper::airlinesAvailabilityDtoToAirlinesAvailability).toList();

        airlinesAvailabilityRepository.saveAll(airlinesAvailabilities);

    }
}
