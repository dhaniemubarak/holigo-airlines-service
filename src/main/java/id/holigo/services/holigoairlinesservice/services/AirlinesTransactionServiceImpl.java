package id.holigo.services.holigoairlinesservice.services;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.web.exceptions.NotFoundException;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AirlinesTransactionServiceImpl implements AirlinesTransactionService {

    private AirlinesFinalFareRepository airlinesFinalFareRepository;

    private ContactPersonRepository contactPersonRepository;

    private ContactPersonMapper contactPersonMapper;

    private AirlinesFinalFareMapper airlinesFinalFareMapper;

    private AirlinesTransactionRepository airlinesTransactionRepository;

    private AirlinesTripRepository airlinesTripRepository;
    private AirlinesTripMapper airlinesTripMapper;

    private AirlinesTripItineraryMapper airlinesTripItineraryMapper;

    private PassengerMapper passengerMapper;

    private PassengerRepository passengerRepository;

    private AirlinesTripPassengerRepository airlinesTripPassengerRepository;

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
    public void setAirlinesTripMapper(AirlinesTripMapper airlinesTripMapper) {
        this.airlinesTripMapper = airlinesTripMapper;
    }

    @Autowired
    public void setAirlinesTripItineraryMapper(AirlinesTripItineraryMapper airlinesTripItineraryMapper) {
        this.airlinesTripItineraryMapper = airlinesTripItineraryMapper;
    }

    @Autowired
    public void setAirlinesTripRepository(AirlinesTripRepository airlinesTripRepository) {
        this.airlinesTripRepository = airlinesTripRepository;
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
    public void setAirlinesTripPassengerRepository(AirlinesTripPassengerRepository airlinesTripPassengerRepository) {
        this.airlinesTripPassengerRepository = airlinesTripPassengerRepository;
    }

    @Transactional
    @Override
    public AirlinesTransaction createTransaction(AirlinesBookDto airlinesBookDto, Long userId) {

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
        AirlinesTransaction savedAirlinesTransaction = airlinesTransactionRepository.save(airlinesTransaction);

        // create passenger
        List<Passenger> passengers = airlinesBookDto.getPassengers().stream()
                .map(passengerMapper::passengerDtoToPassenger).toList();
        Iterable<Passenger> savedPassengers = passengerRepository.saveAll(passengers);
        // End of create passenger

        // create trip
        List<AirlinesTrip> airlinesTrips = new ArrayList<>();
        airlinesFinalFare.getTrips().forEach(airlinesFinalFareTrip -> {
            AirlinesTrip airlinesTrip = airlinesTripMapper.airlinesFinalFareTripToAirlinesTrip(airlinesFinalFareTrip);
            airlinesTrip.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
            airlinesTrip.setOrderStatus(OrderStatusEnum.PROCESS_BOOK);
            airlinesFinalFareTrip.getItineraries().forEach(airlinesFinalFareTripItinerary -> {
                airlinesTrip.addToItineraries(airlinesTripItineraryMapper
                        .airlinesFinalFareItineraryToAirlinesTripItinerary(airlinesFinalFareTripItinerary));
            });
            airlinesTrip.setTransaction(savedAirlinesTransaction);
            airlinesTrips.add(airlinesTrip);
        });
        Iterable<AirlinesTrip> savedAirlinesTrips = airlinesTripRepository.saveAll(airlinesTrips);
        // End of create trip

        // create trip passenger
        List<AirlinesTripPassenger> airlinesTripPassengers = new ArrayList<>();
        // create passengers
        savedAirlinesTrips.forEach(airlinesTrip -> {
            savedPassengers.forEach(passenger -> {
                AirlinesTripPassenger airlinesTripPassenger = new AirlinesTripPassenger();
                airlinesTripPassenger.setTrip(airlinesTrip);
                airlinesTripPassenger.setPassenger(passenger);
                airlinesTripPassengers.add(airlinesTripPassenger);
            });
        });
        airlinesTripPassengerRepository.saveAll(airlinesTripPassengers);
        // End of create trip passenger

        return savedAirlinesTransaction;
    }
}
