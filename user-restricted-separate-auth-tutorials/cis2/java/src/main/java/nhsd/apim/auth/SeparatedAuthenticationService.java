package nhsd.apim.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Component
public class SeparatedAuthenticationService {
    @Value("${auth.provider.client-id}")
    private String providerClientId;
    @Value("${auth.provider.redirect-uri}")
    private String providerRedirectUri;
    @Value("${auth.provider.scope}")
    private String scope;
    @Value("${auth.provider.authorization-uri}")
    private String providerAuthUri;

    private final SeparateAuthRequestClient separateAuthRequestClient;

    public SeparatedAuthenticationService(SeparateAuthRequestClient separateAuthRequestClient) {
        this.separateAuthRequestClient = separateAuthRequestClient;
    }

    public String redirect() {
        return UriComponentsBuilder
                .fromUriString(providerAuthUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", providerClientId)
                .queryParam("state", UUID.randomUUID().toString())
                .queryParam("redirect_uri", providerRedirectUri)
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
