package id.holigo.services.holigoairlinesservice.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FareBadException extends RuntimeException {

    public FareBadException() {
        super();
    }

    public FareBadException(String message, Throwable cause) {
        super(message, cause);
    }

    public FareBadException(String message) {
        super(message);
    }

    public FareBadException(Throwable cause) {
        super(cause);
    }

    public FareBadException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
