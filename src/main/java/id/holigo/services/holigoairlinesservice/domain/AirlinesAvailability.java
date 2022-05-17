package id.holigo.services.holigoairlinesservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airlines_availabilities")
@IdClass(AirlinesScheduleId.class)
public class AirlinesAvailability {

    @Id
    private String airlinesCode;

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

    @Lob
    private String fare;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
