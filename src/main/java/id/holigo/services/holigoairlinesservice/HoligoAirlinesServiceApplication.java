package id.holigo.services.holigoairlinesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HoligoAirlinesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoligoAirlinesServiceApplication.class, args);
    }

}
