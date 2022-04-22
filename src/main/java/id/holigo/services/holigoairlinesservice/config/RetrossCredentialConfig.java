package id.holigo.services.holigoairlinesservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "retross.credential")
@Configuration
public class RetrossCredentialConfig {

    public static String rqid;

    public static String mmid;
}
