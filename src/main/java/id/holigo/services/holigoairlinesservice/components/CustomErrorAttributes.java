package id.holigo.services.holigoairlinesservice.components;

import id.holigo.services.holigoairlinesservice.web.exceptions.BookException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        if (error instanceof BookException) {
            errorAttributes.put("title", "Disini judul");
            errorAttributes.put("imageUrl", "https://ini.gabar.ilustrasi");
            errorAttributes.put("buttonLabel", "Back");
        }
        return errorAttributes;
    }
}
