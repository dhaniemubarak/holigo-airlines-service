package id.holigo.services.holigoairlinesservice.domain;


import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.common.model.TripType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "airlines_transactions")
public class AirlinesTransaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private Long userId;

    @Column(length = 36, columnDefinition = "varchar(36)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID transactionId;

    @OneToOne
    private ContactPerson contactPerson;

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @OneToMany(mappedBy = "transaction")
    private List<AirlinesTrip> trips;

    private Boolean isBookable;

    private Timestamp expiredAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum paymentStatus;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal fareAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal adminAmount;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal discountAmount;

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


}
