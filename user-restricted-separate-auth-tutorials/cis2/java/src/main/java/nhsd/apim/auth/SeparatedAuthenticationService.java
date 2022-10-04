package nhsd.apim.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
public class SeparatedAuthenticationService {
    @Value("${auth.provider.client-id}")
    private String clientId;
    @Value("${auth.provider.redirect-uri}")
    private String redirectUri;
    @Value("${auth.provider.scope}")
    private String scope;
    @Value("${auth.provider.authorization-uri}")
    private String authUri;

    private final SeparateAuthRequestClient separateAuthRequestClient;

    public SeparatedAuthenticationService(SeparateAuthRequestClient separateAuthRequestClient) {
        this.separateAuthRequestClient = separateAuthRequestClient;
    }

    public String redirect() {
        return UriComponentsBuilder
                .fromUriString(authUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("state", UUID.randomUUID().toString())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scope)
                .build().toString();
    }

    public String authenticate(String code) {
        try {
            return separateAuthRequestClient.getToken(code);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Internal Server Error");
        }
    }
}
