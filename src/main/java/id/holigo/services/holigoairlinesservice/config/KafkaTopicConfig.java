package id.holigo.services.holigoairlinesservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String UPDATE_AIRLINES_TRANSACTION = "update-airlines-transaction";

    @Bean
    public NewTopic updateAirlinesTransaction() {
        return TopicBuilder.name(UPDATE_AIRLINES_TRANSACTION).build();
    }
}
