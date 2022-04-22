package id.holigo.services.holigoairlinesservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AirlinesAvailabilities {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(columnDefinition = "varchar(20)")
    private String airlinesName;

    @Column(columnDefinition = "varchar(4)")
    private String originAirportId;

    @Column(columnDefinition = "varchar(4)")
    private String destinationAirportId;

    private Date departureDate;

    private Time departureTime;

    private Date arrivalDate;

    private Time arrivalTime;

    private Integer hpAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal fareAmount;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;
}
