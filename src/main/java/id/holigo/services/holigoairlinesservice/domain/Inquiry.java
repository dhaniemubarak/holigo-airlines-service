package id.holigo.services.holigoairlinesservice.domain;

import id.holigo.services.common.model.TripType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String airlinesCode;

    @Transient
    private String originAirportId;

    @ManyToOne
    private Airport originAirport;

    @Transient
    private String destinationAirportId;

    @ManyToOne
    private Airport destinationAirport;

    private Date departureDate;
    private Date returnDate;
    @Enumerated(EnumType.STRING)
    private TripType tripType;
    private Integer adultAmount;
    private Integer childAmount;
    private Integer infantAmount;
    @Column(length = 1)
    private String seatClass;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
}
