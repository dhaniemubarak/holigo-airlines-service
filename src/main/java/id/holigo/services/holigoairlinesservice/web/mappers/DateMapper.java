package id.holigo.services.holigoairlinesservice.web.mappers;

import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class DateMapper {
    public String asString(Date date) {
        if (date != null) {
            return date.toString();
        }
        return null;
    }

    public Date asDate(String date) {
        if (date != null) {
            return Date.valueOf(date);
        }
        return null;
    }
}
