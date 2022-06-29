package id.holigo.services.holigoairlinesservice.web.model;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.common.model.TripType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class AirlinesTransactionDto implements Serializable {

    private UUID id;

    private Long userId;

    private UUID transactionId;

    private ContactPersonDto contactPerson;

    private TripType tripType;

    private Boolean isBookable;

    private Timestamp expiredAt;

    private PaymentStatusEnum paymentStatus;

    private BigDecimal fareAmount;

    private BigDecimal adminAmount;

    private BigDecimal discountAmount;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal cpAmount;

    private BigDecimal mpAmount;

    private BigDecimal ipAmount;

    private BigDecimal hpAmount;

    private BigDecimal hvAmount;

    private BigDecimal prAmount;

    private BigDecimal ipcAmount;

    private BigDecimal hpcAmount;

    private BigDecimal prcAmount;

    private BigDecimal lossAmount;

    private Timestamp createdAt;

    private Timestamp updatedAt;
}
