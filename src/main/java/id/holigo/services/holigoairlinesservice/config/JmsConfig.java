package id.holigo.services.holigoairlinesservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class JmsConfig {

    public static final String CREATE_NEW_TRANSACTION = "create-new-transaction";
    public static final String GET_DETAIL_FARE_PRODUCT = "get-detail-fare-product";
    public static final String SET_ORDER_STATUS_BY_TRANSACTION_ID_TYPE = "set-order-status-transaction-by-transaction-id-type";

    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
