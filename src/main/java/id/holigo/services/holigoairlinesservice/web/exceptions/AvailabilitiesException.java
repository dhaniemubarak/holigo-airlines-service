package id.holigo.services.holigoairlinesservice.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AvailabilitiesException extends RuntimeException {

    public AvailabilitiesException() {
        super();
    }

//    public AvailabilitiesException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public AvailabilitiesException(String message) {
        super(message);
    }

    public AvailabilitiesException(Throwable cause) {
        super(cause);
    }

    public AvailabilitiesException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
