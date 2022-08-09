package id.holigo.services.holigoairlinesservice.components;

import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import id.holigo.services.holigoairlinesservice.web.exceptions.FareBadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Value("${airlines.exception.book-exception-image-url}")
    private String bookException;

    @Value("${airlines.exception.fare-exception-image-url}")
    private String fareException;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        if (error instanceof BookException) {
            errorAttributes.put("title", "");
            errorAttributes.put("imageUrl", bookException);
            errorAttributes.put("buttonLabel", "Back");
            errorAttributes.put("route", "back");
        }
        if (error instanceof FareBadException) {
            errorAttributes.put("title", "Sesinya habis nih");
            errorAttributes.put("imageUrl", fareException);
            errorAttributes.put("buttonLabel", "Cari lagi ");
            errorAttributes.put("route", "back");
        }
        return errorAttributes;
    }
}
