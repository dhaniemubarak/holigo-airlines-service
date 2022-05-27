package id.holigo.services.holigoairlinesservice.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "airlines_final_fares")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirlinesFinalFare {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private Long userId;

    private String indexNote;

    private String valueNote;

    private Boolean isBookable;

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

    @OneToMany(mappedBy = "finalFare", cascade = CascadeType.ALL)
    private List<AirlinesFinalFareTrip> trips = new ArrayList<>();
}
