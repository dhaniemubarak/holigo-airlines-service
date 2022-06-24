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
import java.util.UUID;

@Getter
@Setter
@Entity(name = "airlines_final_fare_itineraries")
public class AirlinesFinalFareTripItinerary {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    private AirlinesFinalFareTrip trip;

    private String airlinesCode;

    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;

    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;


    @ManyToOne
    private Airport originAirport;

    @ManyToOne
    private Airport destinationAirport;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
