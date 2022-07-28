package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.common.model.TransactionDto;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import id.holigo.services.holigoairlinesservice.web.exceptions.NotFoundException;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AirlinesTransactionServiceImpl implements AirlinesTransactionService {

    private AirlinesFinalFareRepository airlinesFinalFareRepository;

    private ContactPersonRepository contactPersonRepository;

    private ContactPersonMapper contactPersonMapper;

    private AirlinesFinalFareMapper airlinesFinalFareMapper;

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesTransactionTripRepository airlinesTransactionTripRepository;
    private AirlinesTransactionTripMapper airlinesTransactionTripMapper;

    private AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper;

    private PassengerMapper passengerMapper;

    private PassengerRepository passengerRepository;

    private AirlinesTransactionTripPassengerRepository airlinesTransactionTripPassengerRepository;

    private AirlinesTransactionMapper airlinesTransactionMapper;

    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    public void setAirlinesTransactionMapper(AirlinesTransactionMapper airlinesTransactionMapper) {
        this.airlinesTransactionMapper = airlinesTransactionMapper;
    }

    @Autowired
    public void setAirlinesFinalFareRepository(AirlinesFinalFareRepository airlinesFinalFareRepository) {
        this.airlinesFinalFareRepository = airlinesFinalFareRepository;
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
    public void setAirlinesFinalFareMapper(AirlinesFinalFareMapper airlinesFinalFareMapper) {
        this.airlinesFinalFareMapper = airlinesFinalFareMapper;
    }

    @Autowired
    public void setAirlinesTransactionRepository(AirlinesTransactionRepository airlinesTransactionRepository) {
        this.airlinesTransactionRepository = airlinesTransactionRepository;
    }

    @Autowired
    public void setAirlinesTripMapper(AirlinesTransactionTripMapper airlinesTransactionTripMapper) {
        this.airlinesTransactionTripMapper = airlinesTransactionTripMapper;
    }

    @Autowired
    public void setAirlinesTripItineraryMapper(AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper) {
        this.airlinesTransactionTripItineraryMapper = airlinesTransactionTripItineraryMapper;
    }

    @Autowired
    public void setAirlinesTripRepository(AirlinesTransactionTripRepository airlinesTransactionTripRepository) {
        this.airlinesTransactionTripRepository = airlinesTransactionTripRepository;
    }

    @Autowired
    public void setPassengerMapper(PassengerMapper passengerMapper) {
        this.passengerMapper = passengerMapper;
    }

    @Autowired
    public void setPassengerRepository(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    @Autowired
    public void setAirlinesTripPassengerRepository(AirlinesTransactionTripPassengerRepository airlinesTransactionTripPassengerRepository) {
        this.airlinesTransactionTripPassengerRepository = airlinesTransactionTripPassengerRepository;
    }

    @Transactional
    @Override
    public TransactionDto createTransaction(AirlinesBookDto airlinesBookDto, Long userId) {
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
        AirlinesTransaction airlinesTransaction = airlinesFinalFareMapper.airlinesFinalFareToAirlinesTransaction(airlinesFinalFare);
        airlinesTransaction.setContactPerson(savedContactPerson);

        // SET EXPIRED
        airlinesTransaction.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30L)));
        airlinesTransaction.setDiscountAmount(BigDecimal.valueOf(0.00));
        airlinesTransaction.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
        airlinesTransaction.setOrderStatus(OrderStatusEnum.PROCESS_BOOK);
        AirlinesTransaction savedAirlinesTransaction = airlinesTransactionRepository.save(airlinesTransaction);

        // create passenger
        List<Passenger> passengers = airlinesBookDto.getPassengers().stream()
                .map(passengerMapper::passengerDtoToPassenger).toList();
        Iterable<Passenger> savedPassengers = passengerRepository.saveAll(passengers);
        // End of create passenger

        // create trip
        List<AirlinesTransactionTrip> airlinesTransactionTrips = new ArrayList<>();
        airlinesFinalFare.getTrips().forEach(airlinesFinalFareTrip -> {
            AirlinesTransactionTrip airlinesTransactionTrip = airlinesTransactionTripMapper.airlinesFinalFareTripToAirlinesTransactionTrip(airlinesFinalFareTrip);
            airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
            airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.PROCESS_BOOK);
            airlinesTransactionTrip.setIsInternational(false);
            airlinesFinalFareTrip.getItineraries().forEach(airlinesFinalFareTripItinerary -> {
                airlinesTransactionTrip.addToItineraries(airlinesTransactionTripItineraryMapper
                        .airlinesFinalFareItineraryToAirlinesTripItinerary(airlinesFinalFareTripItinerary));
            });
            airlinesTransactionTrip.setTransaction(savedAirlinesTransaction);
            airlinesTransactionTrips.add(airlinesTransactionTrip);
        });
        Iterable<AirlinesTransactionTrip> savedAirlinesTrips = airlinesTransactionTripRepository.saveAll(airlinesTransactionTrips);
        // End of create trip

        // create trip passenger
        List<AirlinesTransactionTripPassenger> airlinesTransactionTripPassengers = new ArrayList<>();
        // create passengers
        savedAirlinesTrips.forEach(airlinesTrip -> {
            savedAirlinesTransaction.addTrip(airlinesTrip);
            savedPassengers.forEach(passenger -> {
                AirlinesTransactionTripPassenger airlinesTransactionTripPassenger = new AirlinesTransactionTripPassenger();
                airlinesTransactionTripPassenger.setTrip(airlinesTrip);
                airlinesTransactionTripPassenger.setPassenger(passenger);
                airlinesTransactionTripPassengers.add(airlinesTransactionTripPassenger);
            });
        });
        airlinesTransactionTripPassengerRepository.saveAll(airlinesTransactionTripPassengers);

        TransactionDto transactionDto = airlinesTransactionMapper.airlinesTransactionToTransactionDto(savedAirlinesTransaction);

        try {
            transactionDto = transactionService.createNewTransaction(transactionDto);
            savedAirlinesTransaction.setTransactionId(transactionDto.getId());
            airlinesTransactionRepository.save(savedAirlinesTransaction);
        } catch (JMSException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return transactionDto;
    }


}
