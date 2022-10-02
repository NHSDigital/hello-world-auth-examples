package nhsd.apim;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhsd.apim.auth.JwtGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class AppConfig {
    @Value("${auth.provider.private-key-path}")
    private String providerPrivateKeyPath;

    @Value("${auth.service.private-key-path}")
    private String servicePrivateKeyPath;

    @Bean
    public JwtGenerator providerJwtGen() throws IOException {
        return new JwtGenerator(providerPrivateKeyPath);
    }

    @Bean
    public JwtGenerator serviceJwtGen() throws IOException {
        return new JwtGenerator(servicePrivateKeyPath);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
