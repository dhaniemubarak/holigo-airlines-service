package id.holigo.services.holigoairlinesservice.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatusCheck {

//    @Scheduled(fixedDelay = 60000)
    public void deleteAirlinesAvailability() {
        /**
         * {
         *     "notrx":"AIR1230106443079",
         *     "mmid": "holigo",
         *     "rqid": "H0LJSHRG3754875Y4698NKJWEF8UHIGO",
         *     "app": "information",
         *     "action": "get_trx_detail"
         * }
         */
    }
}
