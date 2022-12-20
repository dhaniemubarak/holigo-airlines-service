package id.holigo.services.holigoairlinesservice.schedulers;

import id.holigo.services.holigoairlinesservice.components.Airlines;
import id.holigo.services.holigoairlinesservice.domain.AirlinesAvailability;
import id.holigo.services.holigoairlinesservice.repositories.AirlinesAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AirlinesAvailableSchedule {

    private final AirlinesAvailabilityRepository airlinesAvailabilityRepository;

    @Scheduled(fixedDelay = 10000)
    public void deleteAirlinesAvailability() {
        Timestamp time = Timestamp.valueOf(LocalDateTime.now().minusMinutes(60L));
        List<AirlinesAvailability> airlinesAvailabilities = airlinesAvailabilityRepository.findAllByCreatedAtLessThan(time);
        if (airlinesAvailabilities.size() > 0) {
            airlinesAvailabilityRepository.deleteAll(airlinesAvailabilities);
        }
        List<AirlinesAvailability> airlinesAvailabilitiesNull = airlinesAvailabilityRepository.findAllByCreatedAtIsNull();
        if (airlinesAvailabilitiesNull.size() > 0) {
            airlinesAvailabilityRepository.deleteAll(airlinesAvailabilitiesNull);
        }
    }
}
