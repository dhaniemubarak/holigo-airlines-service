package id.holigo.services.holigoairlinesservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import id.holigo.services.common.model.*;
import id.holigo.services.holigoairlinesservice.domain.*;
import id.holigo.services.holigoairlinesservice.repositories.*;
import id.holigo.services.holigoairlinesservice.services.transaction.TransactionService;
import id.holigo.services.holigoairlinesservice.services.user.UserService;
import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import id.holigo.services.holigoairlinesservice.web.exceptions.NotFoundException;
import id.holigo.services.holigoairlinesservice.web.mappers.*;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesBookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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

    private UserService userService;

    private AirlinesService airlinesService;

    @Autowired
    public void setAirlinesService(AirlinesService airlinesService) {
        this.airlinesService = airlinesService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAirlinesTransactionTripPassengerRepository(AirlinesTransactionTripPassengerRepository airlinesTransactionTripPassengerRepository) {
        this.airlinesTransactionTripPassengerRepository = airlinesTransactionTripPassengerRepository;
    }

    @Autowired
    public void setAirlinesTransactionTripMapper(AirlinesTransactionTripMapper airlinesTransactionTripMapper) {
        this.airlinesTransactionTripMapper = airlinesTransactionTripMapper;
    }

    @Autowired
    public void setAirlinesTransactionTripItineraryMapper(AirlinesTransactionTripItineraryMapper airlinesTransactionTripItineraryMapper) {
        this.airlinesTransactionTripItineraryMapper = airlinesTransactionTripItineraryMapper;
    }

    @Autowired
    public void setAirlinesTransactionTripRepository(AirlinesTransactionTripRepository airlinesTransactionTripRepository) {
        this.airlinesTransactionTripRepository = airlinesTransactionTripRepository;
    }

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

    //    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public TransactionDto createTransaction(AirlinesBookDto airlinesBookDto, Long userId) {
        String indexUser;
        String indexProduct;
        String indexPassenger;
        AirlinesFinalFare airlinesFinalFare;
        Optional<AirlinesFinalFare> fetchFinalFare = airlinesFinalFareRepository.findById(airlinesBookDto.getFareId());
        if (fetchFinalFare.isPresent()) {
            airlinesFinalFare = fetchFinalFare.get();
        } else {
            throw new NotFoundException("Flight not available");
        }
        UserDtoForUser userDtoForUser = userService.getUser(userId);
        String userGroup;
        switch (userDtoForUser.getUserGroup()) {
            case MEMBER -> userGroup = "Member";
            case NETIZEN -> userGroup = "Netizen";
            case BOSSQIU -> userGroup = "BossQiu";
            case SOELTAN -> userGroup = "Soeltan";
            case CRAZY_RICH -> userGroup = "Crazy Rich";
            default -> userGroup = "Guest";
        }
        indexUser = userDtoForUser.getName() + "|" + userDtoForUser.getPhoneNumber() + "|" + userGroup;
        // create contact person
        ContactPerson savedContactPerson = contactPersonRepository
                .save(contactPersonMapper.contactPersonDtoToContactPerson(
                        airlinesBookDto.getContactPerson()));
        // create passenger
        List<Passenger> passengers = airlinesBookDto.getPassengers().stream()
                .map(passengerMapper::passengerDtoToPassenger).toList();
        Iterable<Passenger> savedPassengers = passengerRepository.saveAll(passengers);
        List<String> stringPassenger = new ArrayList<>();
        passengers.forEach(passenger -> {
            String type;
            switch (passenger.getType()) {
                case CHILD -> type = "Anak";
                case INFANT -> type = "Bayi";
                default -> type = "Dewasa";
            }
            String title = passenger.getTitle().toString().toLowerCase();
            stringPassenger.add(title.substring(0, 1).toUpperCase() + title.substring(1) + ". " + passenger.getName() + " (" + type + ")");
        });
        indexPassenger = String.join("|", stringPassenger);
        // Create airlines transaction
        AirlinesTransaction airlinesTransaction = airlinesFinalFareMapper.airlinesFinalFareToAirlinesTransaction(airlinesFinalFare);
        airlinesTransaction.setContactPerson(savedContactPerson);
        airlinesTransaction.setIndexUser(indexUser);
        airlinesTransaction.setIndexPassenger(indexPassenger);

        // SET EXPIRED
        airlinesTransaction.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30L)));
        airlinesTransaction.setDiscountAmount(BigDecimal.valueOf(0.00));
        airlinesTransaction.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
        airlinesTransaction.setOrderStatus(OrderStatusEnum.PROCESS_BOOK);


        // CREATE INDEX PRODUCT
        if (airlinesTransaction.getTripType().equals(TripType.O)) {
            indexProduct = airlinesTransaction.getTripType().toString() + "|"
                    + airlinesFinalFare.getTrips().get(0).getOriginAirport().getId() + "-" + airlinesFinalFare.getTrips().get(0).getDestinationAirport().getId() + "|"
                    + airlinesFinalFare.getTrips().get(0).getDepartureDate().toString() + " " + airlinesFinalFare.getTrips().get(0).getDepartureTime().toString() + "|"
                    + airlinesFinalFare.getTrips().get(0).getAirlinesName() + "|"
                    + airlinesFinalFare.getTrips().get(0).getAdultAmount() + "," + airlinesFinalFare.getTrips().get(0).getChildAmount() + "," + airlinesFinalFare.getTrips().get(0).getInfantAmount() + "|";
        } else {
            indexProduct = airlinesTransaction.getTripType().toString() + "|"
                    + airlinesFinalFare.getTrips().get(0).getOriginAirport().getId() + "-" + airlinesFinalFare.getTrips().get(0).getDestinationAirport().getId() + "|"
                    + airlinesFinalFare.getTrips().get(0).getDepartureDate().toString() + " " + airlinesFinalFare.getTrips().get(0).getDepartureTime().toString() + "," + airlinesFinalFare.getTrips().get(1).getDepartureDate().toString() + " " + airlinesFinalFare.getTrips().get(1).getDepartureTime().toString() + "|"
                    + airlinesFinalFare.getTrips().get(0).getAirlinesName() + "," + airlinesFinalFare.getTrips().get(1).getAirlinesName() + "|"
                    + airlinesFinalFare.getTrips().get(0).getAdultAmount() + "," + airlinesFinalFare.getTrips().get(0).getChildAmount() + "," + airlinesFinalFare.getTrips().get(0).getInfantAmount() + "|";
        }
        airlinesTransaction.setIndexProduct(indexProduct);
        AirlinesTransaction savedAirlinesTransaction = airlinesTransactionRepository.save(airlinesTransaction);

        // create trip
        List<AirlinesTransactionTrip> airlinesTransactionTrips = new ArrayList<>();
        airlinesFinalFare.getTrips().forEach(airlinesFinalFareTrip -> {
            AirlinesTransactionTrip airlinesTransactionTrip = airlinesTransactionTripMapper.airlinesFinalFareTripToAirlinesTransactionTrip(airlinesFinalFareTrip);
            airlinesTransactionTrip.setPaymentStatus(PaymentStatusEnum.SELECTING_PAYMENT);
            airlinesTransactionTrip.setOrderStatus(OrderStatusEnum.PROCESS_BOOK);
            airlinesTransactionTrip.setIsInternational(false);
            airlinesFinalFareTrip.getItineraries().forEach(airlinesFinalFareTripItinerary -> airlinesTransactionTrip.addToItineraries(airlinesTransactionTripItineraryMapper
                    .airlinesFinalFareItineraryToAirlinesTripItinerary(airlinesFinalFareTripItinerary)));
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

        // BOOKED TO SUPPLIER

        try {
            AirlinesTransaction bookAirlinesTransaction = airlinesService.createBook(savedAirlinesTransaction.getId());
            if (!bookAirlinesTransaction.getOrderStatus().equals(OrderStatusEnum.BOOKED)) {
                throw new BookException("Gagal booking, Silahkan pilih kembali penerbangan Anda", null, false, false);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        TransactionDto transactionDto = airlinesTransactionMapper.airlinesTransactionToTransactionDto(savedAirlinesTransaction);

        try {
            transactionDto = transactionService.createNewTransaction(transactionDto);
            savedAirlinesTransaction.setTransactionId(transactionDto.getId());
            savedAirlinesTransaction.setInvoiceNumber(transactionDto.getInvoiceNumber());
            airlinesTransactionRepository.save(savedAirlinesTransaction);
        } catch (JMSException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return transactionDto;
    }


}
