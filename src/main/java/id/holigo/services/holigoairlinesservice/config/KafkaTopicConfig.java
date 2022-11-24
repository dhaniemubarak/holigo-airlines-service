package id.holigo.services.holigoairlinesservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String UPDATE_ORDER_STATUS_AIRLINES_TRANSACTION = "update-order-status-airlines-transaction";

    public static final String UPDATE_PAYMENT_STATUS_AIRLINES_TRANSACTION = "update-payment-status-airlines-transaction";

    @Bean
    public NewTopic updateOrderStatusAirlinesTransaction() {
        return TopicBuilder.name(UPDATE_ORDER_STATUS_AIRLINES_TRANSACTION).build();
    }

    @Bean
    public NewTopic updateStatusPaymentAirlinesTransaction() {
        return TopicBuilder.name(UPDATE_PAYMENT_STATUS_AIRLINES_TRANSACTION).build();
    }
}
