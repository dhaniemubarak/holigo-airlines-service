package id.holigo.services.holigoairlinesservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airlines_availabilities")
@IdClass(AirlinesScheduleId.class)
public class AirlinesAvailability {

    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Id
    private String airlinesCode;

    @OneToMany(mappedBy = "airlinesAvailability", cascade = CascadeType.ALL)
    private List<AirlinesAvailabilityFare> fares = new ArrayList<>();

    @OneToMany(mappedBy = "airlinesAvailability", cascade = CascadeType.ALL)
    @OrderBy("leg")
    private List<AirlinesAvailabilityItinerary> itineraries = new ArrayList<>();

    @Id
    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;

    @Id
    @Column(columnDefinition = "varchar(20)")
    private String flightNumber;

    @Id
    @Column(columnDefinition = "varchar(4)")
    private String originAirportId;

    @Id
    @Column(columnDefinition = "varchar(4)")
    private String destinationAirportId;

    @Id
    private Date departureDate;

    @Id
    private Time departureTime;

    @Id
    private Date arrivalDate;

    @Id
    private Time arrivalTime;

    private Integer duration;

    private String imageUrl;

    private Integer transit;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    public void addToItineraries(AirlinesAvailabilityItinerary itinerary) {
        itinerary.setAirlinesAvailability(this);
        this.itineraries.add(itinerary);
    }

    public void addToFares(AirlinesAvailabilityFare airlinesAvailabilityFare) {
        airlinesAvailabilityFare.setAirlinesAvailability(this);
        this.fares.add(airlinesAvailabilityFare);
    }
}
