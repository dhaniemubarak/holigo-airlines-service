package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetrossFareDto {

    private String subClass;

    private Integer seatAvb;

    private BigDecimal nta;

    private BigDecimal totalFare;

    private String selectedIdDep;
}
