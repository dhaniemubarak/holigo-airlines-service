package id.holigo.services.holigoairlinesservice.components;

import id.holigo.services.holigoairlinesservice.web.model.TripDto;
import org.springframework.stereotype.Component;

@Component
public class AirlinesRequireIdentityNumber {

    public boolean isIdentityNumberRequired(TripDto tripDto) {
        return switch (tripDto.getInquiry().getAirlinesCode()) {
            case "JT", "SJ" -> true;
            default -> false;
        };
    }
}
