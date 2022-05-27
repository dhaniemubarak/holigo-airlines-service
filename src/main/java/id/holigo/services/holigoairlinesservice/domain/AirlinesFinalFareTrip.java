package id.holigo.services.holigoairlinesservice.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "airlines_final_fare_trips")
public class AirlinesFinalFareTrip {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    private AirlinesFinalFare finalFare;
    private String airlinesCode;
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<AirlinesFinalFareTripItinerary> itineraries = new ArrayList<>();
    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;
    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;
    @Column(columnDefinition = "varchar(4)")
    private String originAirportId;
    @Column(columnDefinition = "varchar(4)")
    private String destinationAirportId;
    private Date departureDate;
    private Time departureTime;
    private Date arrivalDate;
    private Time arrivalTime;
    private Integer duration;
    private String imageUrl;
    private Integer transit;
    @Column(length = 4, columnDefinition = "tinyint(2) default 1", nullable = false)
    private Integer adultAmount;

    @Column(length = 2, columnDefinition = "tinyint(2) default 0", nullable = false)
    private Integer childAmount;

    @Column(length = 2, columnDefinition = "tinyint(2) default 0", nullable = false)
    private Integer infantAmount;

    private Boolean isBookable;

    @Column(columnDefinition = "tinyint(1) default 0")
    private Boolean isInternational;

    @Column(columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean isPriceIncluded;

    private Boolean isIdentityNumberRequired;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal fareAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal adminAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal ntaAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal nraAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal cpAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal mpAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal ipAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal hpAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal hvAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal prAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal ipcAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal hpcAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal prcAmount;

    @Column(columnDefinition = "decimal(10,2) default 0")
    private BigDecimal lossAmount;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    private String supplierId;

    public void addToItineraries(AirlinesFinalFareTripItinerary itinerary) {
        itinerary.setTrip(this);
        this.itineraries.add(itinerary);
    }
}
