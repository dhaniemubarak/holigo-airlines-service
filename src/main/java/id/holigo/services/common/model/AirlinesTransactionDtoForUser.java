package id.holigo.services.common.model;

import id.holigo.services.common.model.PaymentStatusEnum;
import id.holigo.services.common.model.TripType;
import id.holigo.services.holigoairlinesservice.web.model.AirlinesTransactionTripDtoForUser;
import id.holigo.services.holigoairlinesservice.web.model.ContactPersonDto;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirlinesTransactionDtoForUser implements Serializable {
    private Long id;

    private String iconUrl;

    private Long userId;

    private ContactPersonDto contactPerson;

    private TripType tripType;

    private Boolean isBookable;

    private Timestamp expiredAt;

    private PaymentStatusEnum paymentStatus;

    private OrderStatusEnum orderStatus;

    private BigDecimal fareAmount;

    private BigDecimal adminAmount;

    private BigDecimal discountAmount;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private BigDecimal hpAmount;

    private BigDecimal hpcAmount;

    private List<AirlinesTransactionTripDtoForUser> trips;

}
