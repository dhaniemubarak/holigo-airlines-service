package id.holigo.services.holigoairlinesservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "airlines_availability_fares")
public class AirlinesAvailabilityFare {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

//    @ManyToOne
//    @JoinColumn(name = "airlines_availability_id")
//    private AirlinesAvailability airlinesAvailability;

    @Column(length = 10, columnDefinition = "varchar(10)")
    private String subclass;

    private Integer seatAvailable;

    private BigDecimal fareAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
