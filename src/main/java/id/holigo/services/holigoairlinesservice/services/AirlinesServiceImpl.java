package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.FareDetailDto;
import id.holigo.services.common.model.FareDto;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.services.fare.FareService;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import id.holigo.services.holigoairlinesservice.web.exceptions.ConflictException;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
public class AirlinesServiceImpl implements AirlinesService {

    private FareService fareService;

    private RetrossAirlinesService retrossAirlinesService;

    private AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    private AirlinesFinalFareRepository airlinesFinalFareRepository;

    private AirlinesAvailabilityMapper airlinesAvailabilityMapper;

    private AirlinesFinalFareTripMapper airlinesFinalFareTripMapper;

    private AirlinesFinalFareTripRepository airlinesFinalFareTripRepository;

    private AirlinesTransactionTripItineraryRepository airlinesTransactionTripItineraryRepository;

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesTransactionTripRepository airlinesTransactionTripRepository;

    @Autowired
    public void setAirlinesTransactionTripRepository(AirlinesTransactionTripRepository airlinesTransactionTripRepository) {
        this.airlinesTransactionTripRepository = airlinesTransactionTripRepository;
    }

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Autowired
    public void setAirlinesTransactionTripItineraryRepository(AirlinesTransactionTripItineraryRepository airlinesTransactionTripItineraryRepository) {
        this.airlinesTransactionTripItineraryRepository = airlinesTransactionTripItineraryRepository;
    }

    private ObjectMapper objectMapper;

    @Autowired
    public void setRetrossAirlinesService(RetrossAirlinesService retrossAirlinesService) {
        this.retrossAirlinesService = retrossAirlinesService;
    }

    @Autowired
    public void setAirlinesAvailabilityRepository(AirlinesAvailabilityRepository airlinesAvailabilityRepository) {
        this.airlinesAvailabilityRepository = airlinesAvailabilityRepository;
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
    public void setAirlinesFinalFareTripMapper(AirlinesFinalFareTripMapper airlinesFinalFareTripMapper) {
        this.airlinesFinalFareTripMapper = airlinesFinalFareTripMapper;
    }

    @Autowired
    public void setAirlinesFinalFareTripRepository(AirlinesFinalFareTripRepository airlinesFinalFareTripRepository) {
        this.airlinesFinalFareTripRepository = airlinesFinalFareTripRepository;
    }

    @Autowired
    public void setFareService(FareService fareService) {
        this.fareService = fareService;
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ListAvailabilityDto getAvailabilities(InquiryDto inquiryDto) throws JsonProcessingException {
        RequestScheduleDto requestScheduleDto = RequestScheduleDto.builder()
                .ac(inquiryDto.getAirlinesCode())
                .org(inquiryDto.getOriginAirport().getId())
                .des(inquiryDto.getDestinationAirport().getId())
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
        ListAvailabilityDto listAvailabilityDto = airlinesAvailabilityMapper.responseScheduleDtoToListAvailabilityDto(responseScheduleDto, inquiryDto);
        saveAvailabilities(listAvailabilityDto);
        return listAvailabilityDto;
    }

    @Transactional
    @Override
    public AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId) {

        Set<AirlinesFinalFareTrip> airlinesFinalFareTrips = new HashSet<>();
        List<List<RetrossFinalFareDepartureDto>> retrossFinalFares = new ArrayList<>();

        TripType tripType = TripType.O;
        for (int i = 0; i < requestFinalFareDto.getTrips().size(); i++) {
            String fares;
            FareDto fareDto;
            ResponseFareDto responseFareDto = null;
            TripDto tripDto = requestFinalFareDto.getTrips().get(i);
            tripType = tripDto.getInquiry().getTripType();
            if ((tripType != TripType.R && i != 1)
                    || (tripType.equals(TripType.R) && i == 0)) {
                Map<String, String> roundTrip = new HashMap<>();
                if (tripType.equals(TripType.R)) {
                    TripDto roundTripDto = requestFinalFareDto.getTrips().get(1);
                    roundTrip.put("airlinesCode", roundTripDto.getTrip().getAirlinesCode());
                    roundTrip.put("selectedId", roundTripDto.getTrip().getFare().getSelectedId());
                }
                try {
                    responseFareDto = retrossAirlinesService.getFare(tripDto, roundTrip);
                    if (!responseFareDto.getError_code().equals("000")) {
                        throw new Exception("Failed get final fare from airlines");
                    }
                    retrossFinalFares.add(responseFareDto.getSchedule().getDepartures());
                    if (tripType.equals(TripType.R)) {
                        retrossFinalFares.add(responseFareDto.getSchedule().getReturns());
                    }
                } catch (Exception e) {
                    airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                            tripDto.getTrip().getAirlinesCode(), tripDto.getInquiry().getOriginAirport().getId(), tripDto.getInquiry().getDestinationAirport().getId(),
                            tripDto.getInquiry().getDepartureDate().toString()
                    );
                    if (tripType.equals(TripType.R)) {
                        airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                                tripDto.getTrip().getAirlinesCode(), tripDto.getInquiry().getDestinationAirport().getId(), tripDto.getInquiry().getOriginAirport().getId(),
                                tripDto.getInquiry().getReturnDate().toString()
                        );
                    }
                    throw new ConflictException(e.getMessage());
                }
            }
            try {
                BigDecimal ntaAmount = retrossFinalFares.get(i).get(0).getFares().get(0).getNta();
                if (ntaAmount.equals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP))) {
                    ntaAmount = retrossFinalFares.get(i).get(0).getFares().get(0).getTotalFare();
                }
                BigDecimal nraAmount = retrossFinalFares.get(i).get(0).getFares().get(0).getTotalFare().subtract(ntaAmount);
                fareDto = fareService.getFareDetail(FareDetailDto.builder()
                        .ntaAmount(ntaAmount)
                        .nraAmount(nraAmount)
                        .productId(1).userId(userId).build());
                fares = objectMapper.writeValueAsString(retrossFinalFares.get(i).get(0).getFares());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            AirlinesAvailability airlinesAvailability = airlinesAvailabilityRepository
                    .getAirlinesAvailabilityById(tripDto.getTrip().getId().toString());
            AirlinesFinalFareTrip airlinesFinalFareTrip = airlinesFinalFareTripMapper
                    .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability, i + 1);
            assert responseFareDto != null;
            responseFareDto.getSchedule().getDepartures().forEach(departureDto -> {
                RetrossFareDto retrossFareDto = departureDto.getFares().get(0);
                airlinesFinalFareTrip.setSupplierId(retrossFareDto.getSelectedIdDep() != null ? retrossFareDto.getSelectedIdDep() : retrossFareDto.getSelectedIdRet());
            });
            airlinesFinalFareTrip.setAdultAmount(tripDto.getInquiry().getAdultAmount());
            airlinesFinalFareTrip.setChildAmount(tripDto.getInquiry().getChildAmount());
            airlinesFinalFareTrip.setInfantAmount(tripDto.getInquiry().getInfantAmount());
            airlinesFinalFareTrip.setFares(fares);
            airlinesFinalFareTrip.setIsPriceIncluded(fareDto.getFareAmount().equals(BigDecimal.valueOf(0.00)));
            airlinesFinalFareTrip.setFareAmount(fareDto.getFareAmount());
            airlinesFinalFareTrip.setNtaAmount(fareDto.getNtaAmount());
            airlinesFinalFareTrip.setNraAmount(fareDto.getNraAmount());
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
        airlinesFinalFare.setTripType(tripType);
        airlinesFinalFare.setIsIdentityNumberRequired(true);
        airlinesFinalFare.setIsPhoneNumberRequired(true);
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

    @Override
    public void createBook(Long airlinesTransactionId) throws JsonProcessingException {
        AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(airlinesTransactionId);
        ResponseBookDto responseBookDto = retrossAirlinesService.createBook(airlinesTransaction);
        if (responseBookDto.getError_code().equals("000")) {
            var wrapper = new Object() {
                int i = 0;
            };
            List<AirlinesTransactionTripItinerary> airlinesTransactionTripItineraries = new ArrayList<>();
            List<AirlinesTransactionTrip> airlinesTransactionTrips = new ArrayList<>();
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setSupplierTransactionId(responseBookDto.getNotrx());
                airlinesTransactionTrips.add(airlinesTransactionTrip);
                for (AirlinesTransactionTripItinerary airlinesTransactionTripItinerary : airlinesTransactionTrip.getItineraries()) {
                    if (wrapper.i == 0) {
                        airlinesTransactionTripItinerary.setPnr(responseBookDto.getPnrDep());
                    } else {
                        airlinesTransactionTripItinerary.setPnr(responseBookDto.getPnrRet());
                    }
                    airlinesTransactionTripItineraries.add(airlinesTransactionTripItinerary);
                }
                wrapper.i++;
            });
            airlinesTransaction.setExpiredAt(responseBookDto.getTimelimit());
            airlinesTransactionRepository.save(airlinesTransaction);
            airlinesTransactionTripRepository.saveAll(airlinesTransactionTrips);
            airlinesTransactionTripItineraryRepository.saveAll(airlinesTransactionTripItineraries);
        } else {
            airlinesTransaction.setSupplierMessage(responseBookDto.getError_msg());
            airlinesTransactionRepository.save(airlinesTransaction);
            throw new BookException(responseBookDto.getError_msg(), null, false, false);
        }
    }

    @Override
    public void cancelBook(AirlinesTransaction airlinesTransaction) throws JsonProcessingException {
        retrossAirlinesService.cancelBook(airlinesTransaction);
    }

    @Override
    public void issued(AirlinesTransaction airlinesTransaction) throws JsonProcessingException {
        retrossAirlinesService.issued(airlinesTransaction);
    }

    @Transactional
    @Override
    public void saveAvailabilities(ListAvailabilityDto listAvailabilityDto) {
        airlinesAvailabilityRepository.saveAll(listAvailabilityDto.getDepartures().stream().map(airlinesAvailabilityMapper::airlinesAvailabilityDtoToAirlinesAvailability).toList());
        if (listAvailabilityDto.getReturns() != null) {
            airlinesAvailabilityRepository.saveAll(listAvailabilityDto.getReturns().stream().map(airlinesAvailabilityMapper::airlinesAvailabilityDtoToAirlinesAvailability).toList());
        }
    }
}
