package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FareDetailDto {

    private Long userId;
    
    private Integer productId;

    private BigDecimal nraAmount;

    private BigDecimal ntaAmount;
}
