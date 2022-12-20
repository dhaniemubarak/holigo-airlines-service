package id.holigo.services.holigoairlinesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class HoligoAirlinesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoligoAirlinesServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
