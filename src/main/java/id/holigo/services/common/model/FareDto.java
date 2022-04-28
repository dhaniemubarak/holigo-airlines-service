package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareDto implements Serializable {

    static final long serialVersionUID = 660L;

    private Long id;

    private UserGroupEnum userGroup;

    private Integer productId;

    private BigDecimal ntaAmount;

    private BigDecimal nraAmount;

    private BigDecimal fareAmount;

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
