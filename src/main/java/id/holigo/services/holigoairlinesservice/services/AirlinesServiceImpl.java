package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.holigo.services.common.model.*;
import id.holigo.services.common.model.FareDto;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.events.OrderStatusEvent;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.services.fare.FareService;
import id.holigo.services.holigoairlinesservice.services.retross.RetrossAirlinesService;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import id.holigo.services.holigoairlinesservice.web.exceptions.AvailabilitiesException;
import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import id.holigo.services.holigoairlinesservice.web.exceptions.ConflictException;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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

    private OrderAirlinesTransactionService orderAirlinesTransactionService;

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setOrderAirlinesTransactionService(OrderAirlinesTransactionService orderAirlinesTransactionService) {
        this.orderAirlinesTransactionService = orderAirlinesTransactionService;
    }

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
        ResponseScheduleDto responseScheduleDto = retrossAirlinesService.getSchedule(requestScheduleDto, inquiryDto.getUserId());
        if (responseScheduleDto.getError_code().equals("001")) {
            throw new AvailabilitiesException(responseScheduleDto.getError_msg());
        }
        if (responseScheduleDto.getSchedule() == null) {
            return null;
        }
        ListAvailabilityDto listAvailabilityDto = airlinesAvailabilityMapper.responseScheduleDtoToListAvailabilityDto(responseScheduleDto, inquiryDto);
        saveAvailabilities(listAvailabilityDto);
        return listAvailabilityDto;
    }

    @Transactional(noRollbackFor = ConflictException.class)
    @Override
    public AirlinesFinalFare createFinalFares(RequestFinalFareDto requestFinalFareDto, Long userId) {

        Set<AirlinesFinalFareTrip> airlinesFinalFareTrips = new HashSet<>();
        List<List<RetrossFinalFareDepartureDto>> retrossFinalFares = new ArrayList<>();
        ResponseFareDto responseFareDto = null;
        TripType tripType = TripType.O;
        AtomicInteger indexTrip = new AtomicInteger();
        for (TripDto tripDto : requestFinalFareDto.getTrips()) {
            tripType = tripDto.getInquiry().getTripType();
            if ((tripType != TripType.R && indexTrip.get() != 1)
                    || (tripType.equals(TripType.R) && indexTrip.get() == 0)) {
                responseFareDto = getResponseFareDto(requestFinalFareDto, userId, tripType, tripDto);
                retrossFinalFares.add(responseFareDto.getSchedule().getDepartures());
                if (tripType.equals(TripType.R)) {
                    retrossFinalFares.add(responseFareDto.getSchedule().getReturns());
                }
            }
            indexTrip.getAndIncrement();
        }
        AtomicInteger indexFinalFare = new AtomicInteger();
        for (List<RetrossFinalFareDepartureDto> retrossFinalFareDepartureDtoList : retrossFinalFares) {
            String fares;
            FareDto fareDto;
            TripDto tripDto = requestFinalFareDto.getTrips().get(indexFinalFare.get());
            try {
                BigDecimal ntaAmount = retrossFinalFareDepartureDtoList.get(0).getFares().get(0).getTotalFare();
                if (ntaAmount.equals(BigDecimal.valueOf(0.00).setScale(2, RoundingMode.UP))) {
                    ntaAmount = retrossFinalFareDepartureDtoList.get(0).getFares().get(0).getTotalFare();
                }
                BigDecimal nraAmount = retrossFinalFareDepartureDtoList.get(0).getFares().get(0).getTotalFare().subtract(ntaAmount);
                fareDto = fareService.getFareDetail(FareDetailDto.builder()
                        .ntaAmount(ntaAmount)
                        .nraAmount(nraAmount)
                        .productId(1).userId(userId).build());
                fares = objectMapper.writeValueAsString(retrossFinalFareDepartureDtoList.get(0).getFares());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            AirlinesAvailability airlinesAvailability = airlinesAvailabilityRepository
                    .getAirlinesAvailabilityById(tripDto.getTrip().getId().toString());
            AirlinesFinalFareTrip airlinesFinalFareTrip = airlinesFinalFareTripMapper
                    .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability, indexFinalFare.get() + 1);
            responseFareDto.getSchedule().getDepartures().forEach(departureDto -> {
                RetrossFareDto retrossFareDto = departureDto.getFares().get(0);
                airlinesFinalFareTrip.setSupplierId(retrossFareDto.getSelectedIdDep() != null ? retrossFareDto.getSelectedIdDep() : retrossFareDto.getSelectedIdRet());
            });
            airlinesFinalFareTrip.setAdultAmount(tripDto.getInquiry().getAdultAmount());
            airlinesFinalFareTrip.setChildAmount(tripDto.getInquiry().getChildAmount());
            airlinesFinalFareTrip.setInfantAmount(tripDto.getInquiry().getInfantAmount());
            airlinesFinalFareTrip.setFares(fares);
            setFinalFare(airlinesFinalFareTrip, fareDto);
            airlinesFinalFareTrips.add(airlinesFinalFareTrip);
            indexFinalFare.getAndIncrement();
        }
        assert responseFareDto != null;
        return getAirlinesFinalFare(userId, airlinesFinalFareTrips, tripType, responseFareDto, false);
    }

    private ResponseFareDto getResponseFareDto(RequestFinalFareDto requestFinalFareDto, Long userId, TripType tripType, TripDto tripDto) {
        ResponseFareDto responseFareDto;
        Map<String, String> roundTrip = setRoundTripVariable(requestFinalFareDto, tripType);
        try {
            responseFareDto = retrossAirlinesService.getFare(tripDto, roundTrip, userId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!responseFareDto.getError_code().equals("000")) {
            catchFinalFare(tripDto, tripType);
        }
        return responseFareDto;
    }

    @Transactional
    @Override
    public AirlinesFinalFare createInternationalFinalFare(RequestFinalFareDto requestFinalFareDto, long userId) {
        Set<AirlinesFinalFareTrip> airlinesFinalFareTrips = new HashSet<>();
        ResponseFareDto responseFareDto = null;
        TripType tripType = TripType.O;
        for (int i = 0; i < requestFinalFareDto.getTrips().size(); i++) {
            TripDto tripDto = requestFinalFareDto.getTrips().get(i);
            tripType = tripDto.getInquiry().getTripType();
            if ((tripType != TripType.R && i != 1)
                    || (tripType.equals(TripType.R) && i == 0)) {
                responseFareDto = getResponseFareDto(requestFinalFareDto, userId, tripType, tripDto);
            }
        }
        ResponseFareDto finalResponseFareDto = responseFareDto;
        requestFinalFareDto.getTrips().forEach(withCounter((i, tripDto) -> {

            AirlinesAvailability airlinesAvailability = airlinesAvailabilityRepository
                    .getAirlinesAvailabilityById(tripDto.getTrip().getId().toString());
            AirlinesFinalFareTrip airlinesFinalFareTrip = airlinesFinalFareTripMapper
                    .airlinesAvailabilityToAirlinesFinalFareTrip(airlinesAvailability, i + 1);
            airlinesFinalFareTrip.setAdultAmount(tripDto.getInquiry().getAdultAmount());
            airlinesFinalFareTrip.setChildAmount(tripDto.getInquiry().getChildAmount());
            airlinesFinalFareTrip.setInfantAmount(tripDto.getInquiry().getInfantAmount());
            airlinesFinalFareTrip.setIsPriceIncluded(true);
            airlinesFinalFareTrip.setFareAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setNtaAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setNraAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setAdminAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setCpAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setMpAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setIpAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setHpAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setHvAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setPrAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setIpcAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setHpcAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setPrcAmount(BigDecimal.ZERO);
            airlinesFinalFareTrip.setLossAmount(BigDecimal.ZERO);
            if (i == 0) {
                assert finalResponseFareDto != null;
                BigDecimal ntaAmount = finalResponseFareDto.getNtaAmount();
                if (ntaAmount.compareTo(BigDecimal.ZERO) == 0) {
                    ntaAmount = finalResponseFareDto.getTotalAmount();
                }
                BigDecimal nraAmount = finalResponseFareDto.getTotalAmount().subtract(ntaAmount);
                FareDto fareDto = fareService.getFareDetail(FareDetailDto.builder()
                        .ntaAmount(ntaAmount)
                        .nraAmount(nraAmount)
                        .productId(1).userId(userId).build());
                airlinesFinalFareTrip.setSupplierId(finalResponseFareDto.getTrxId());
                setFinalFare(airlinesFinalFareTrip, fareDto);
            }
            airlinesFinalFareTrips.add(airlinesFinalFareTrip);
        }));
        return getAirlinesFinalFare(userId, airlinesFinalFareTrips, tripType, responseFareDto, true);
    }

    private void catchFinalFare(TripDto tripDto, TripType tripType) {
        airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                tripDto.getTrip().getAirlinesCode(), tripDto.getInquiry().getOriginAirport().getId(), tripDto.getInquiry().getDestinationAirport().getId(),
                tripDto.getInquiry().getDepartureDate());
//        airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere();
        if (tripType.equals(TripType.R)) {
            airlinesAvailabilityRepository.deleteAllAirlinesAvailabilityWhere(
                    tripDto.getTrip().getAirlinesCode(), tripDto.getInquiry().getDestinationAirport().getId(), tripDto.getInquiry().getOriginAirport().getId(),
                    tripDto.getInquiry().getReturnDate());
        }
        throw new ConflictException();
    }

    private AirlinesFinalFare getAirlinesFinalFare(long userId, Set<AirlinesFinalFareTrip> airlinesFinalFareTrips, TripType tripType, ResponseFareDto responseFareDto, Boolean isInternational) {
        FareDto fareDto = fareService.getFareDetail(FareDetailDto.builder()
                .ntaAmount(responseFareDto.getNtaAmount())
                .nraAmount(responseFareDto.getTotalAmount().subtract(responseFareDto.getNtaAmount()))
                .productId(1).userId(userId).build());
        AirlinesFinalFare airlinesFinalFare = AirlinesFinalFare.builder()
                .fareAmount(fareDto.getFareAmount())
                .adminAmount(BigDecimal.valueOf(0.00))
                .ntaAmount(fareDto.getNtaAmount())
                .nraAmount(fareDto.getNraAmount())
                .cpAmount(fareDto.getCpAmount())
                .mpAmount(fareDto.getMpAmount())
                .ipAmount(fareDto.getIpAmount())
                .hpAmount(fareDto.getHpAmount())
                .hvAmount(fareDto.getHvAmount())
                .prAmount(fareDto.getPrAmount())
                .ipcAmount(fareDto.getIpcAmount())
                .hpcAmount(fareDto.getHpcAmount())
                .prcAmount(fareDto.getPrcAmount())
                .lossAmount(fareDto.getLossAmount())
                .isInternational(isInternational)
                .userId(userId)
                .isBookable(true)
                .tripType(tripType)
                .isIdentityNumberRequired(true)
                .isPhoneNumberRequired(true)
                .build();
        AirlinesFinalFare savedAirlinesFinalFare = airlinesFinalFareRepository.save(airlinesFinalFare);
        airlinesFinalFareTrips.forEach(airlinesFinalFareTrip -> airlinesFinalFareTrip.setFinalFare(savedAirlinesFinalFare));
        airlinesFinalFareTripRepository.saveAll(airlinesFinalFareTrips);
        return savedAirlinesFinalFare;
    }

    private void setFinalFare(AirlinesFinalFareTrip airlinesFinalFareTrip, FareDto fareDto) {
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
    }

    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }

    @Override
    public AirlinesTransaction createBook(Long airlinesTransactionId) throws JsonProcessingException {
        AirlinesTransaction airlinesTransaction = airlinesTransactionRepository.getById(airlinesTransactionId);
        ResponseBookDto responseBookDto = retrossAirlinesService.createBook(airlinesTransaction);
        if (responseBookDto.getError_code().equals("000")) {
            AtomicInteger i = new AtomicInteger();
            List<AirlinesTransactionTripItinerary> airlinesTransactionTripItineraries = new ArrayList<>();
            List<AirlinesTransactionTrip> airlinesTransactionTrips = new ArrayList<>();
            airlinesTransaction.getTrips().forEach(airlinesTransactionTrip -> {
                airlinesTransactionTrip.setSupplierTransactionId(responseBookDto.getNotrx());
                airlinesTransactionTrips.add(airlinesTransactionTrip);
                for (AirlinesTransactionTripItinerary airlinesTransactionTripItinerary : airlinesTransactionTrip.getItineraries()) {
                    if (i.get() == 0) {
                        airlinesTransactionTripItinerary.setPnr(responseBookDto.getPnrDep());
                    } else {
                        airlinesTransactionTripItinerary.setPnr(responseBookDto.getPnrRet());
                    }
                    airlinesTransactionTripItineraries.add(airlinesTransactionTripItinerary);
                }
                i.getAndIncrement();
            });
            airlinesTransaction.setExpiredAt(responseBookDto.getTimelimit());
            airlinesTransactionRepository.save(airlinesTransaction);
            airlinesTransactionTripRepository.saveAll(airlinesTransactionTrips);
            airlinesTransactionTripItineraryRepository.saveAll(airlinesTransactionTripItineraries);
            StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.booked(airlinesTransaction.getId());
            if (sm.getState().getId().equals(OrderStatusEnum.BOOKED)) {
                transactionService.updateOrderStatusTransaction(TransactionDto.builder()
                        .orderStatus(OrderStatusEnum.BOOKED)
                        .id(airlinesTransaction.getTransactionId()).build());
            }
        } else {
            airlinesTransaction.setSupplierMessage(responseBookDto.getError_msg());
            airlinesTransactionRepository.save(airlinesTransaction);
            StateMachine<OrderStatusEnum, OrderStatusEvent> sm = orderAirlinesTransactionService.bookFailed(airlinesTransaction.getId());
            if (sm.getState().getId().equals(OrderStatusEnum.BOOK_FAILED)) {
                transactionService.updateOrderStatusTransaction(TransactionDto.builder()
                        .orderStatus(OrderStatusEnum.BOOK_FAILED)
                        .id(airlinesTransaction.getTransactionId()).build());
            }
            throw new BookException(responseBookDto.getError_msg(), null, false, false);
        }
        return airlinesTransaction;
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

    private Map<String, String> setRoundTripVariable(RequestFinalFareDto requestFinalFareDto, TripType tripType) {
        Map<String, String> roundTrip = new HashMap<>();
        if (tripType.equals(TripType.R)) {
            TripDto roundTripDto = requestFinalFareDto.getTrips().get(1);
            roundTrip.put("airlinesCode", roundTripDto.getTrip().getAirlinesCode());
            roundTrip.put("selectedId", roundTripDto.getTrip().getFare().getSelectedId());
        }
        return roundTrip;
    }
}
