package id.holigo.services.holigoairlinesservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import id.holigo.services.holigoairlinesservice.web.model.AirportDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airlines_availability_itineraries")
public class AirlinesAvailabilityItinerary {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private AirlinesAvailability airlinesAvailability;

    private String airlinesCode;

    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;

    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;

    @Column(columnDefinition = "varchar(4)")
    private String originAirportId;

    @Transient
    @ManyToOne
    private Airport originAirport;

    @Column(columnDefinition = "varchar(4)")
    private String destinationAirportId;

    @Transient
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
