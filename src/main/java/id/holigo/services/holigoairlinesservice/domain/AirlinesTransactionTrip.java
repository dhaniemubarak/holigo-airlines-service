package id.holigo.services.holigoairlinesservice.domain;

import id.holigo.services.common.model.OrderStatusEnum;
import id.holigo.services.common.model.PaymentStatusEnum;
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
@Entity(name = "airlines_transaction_trips")
public class AirlinesTransactionTrip {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne
    private AirlinesTransaction transaction;

    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<AirlinesTransactionTripItinerary> itineraries = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<AirlinesTransactionTripPassenger> passengers;

    @Column(columnDefinition = "varchar(4)", length = 4, nullable = false)
    private String airlinesCode;

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

    @Column(length = 4, columnDefinition = "tinyint(2) default 1", nullable = false)
    private Integer adultAmount;

    @Column(length = 2, columnDefinition = "tinyint(2) default 0", nullable = false)
    private Integer childAmount;

    @Column(length = 2, columnDefinition = "tinyint(2) default 0", nullable = false)
    private Integer infantAmount;

    @Column(columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean isPriceIncluded;

    @Column(columnDefinition = "tinyint(1) default 0", nullable = false)
    private Boolean isInternational;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

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

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal lossAmount;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public void setItineraries(List<AirlinesTransactionTripItinerary> itineraries) {
        this.itineraries = itineraries;
    }

    public void addToItineraries(AirlinesTransactionTripItinerary airlinesTransactionTripItinerary) {
        airlinesTransactionTripItinerary.setTrip(this);
        this.itineraries.add(airlinesTransactionTripItinerary);
    }


}
