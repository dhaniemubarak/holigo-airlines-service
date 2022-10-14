package id.holigo.services.holigoairlinesservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "airlines_transaction_trip_itineraries")
public class AirlinesTransactionTripItinerary {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    private AirlinesTransactionTrip trip;

    @Column(length = 10, columnDefinition = "varchar(10)")
    private String pnr;

    @Column(length = 1, columnDefinition = "tinyint(1)")
    private Integer leg;

    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;

    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;

    @ManyToOne
    private Airport originAirport;

    @ManyToOne
    private Airport destinationAirport;

    private LocalDate departureDate;

    private LocalTime departureTime;

    private LocalDate arrivalDate;

    private LocalTime arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
