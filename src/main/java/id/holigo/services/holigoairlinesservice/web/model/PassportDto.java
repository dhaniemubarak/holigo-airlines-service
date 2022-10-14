package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PassportDto implements Serializable {

    private String passportNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String issueCountry;
}
