package nhsd.apim.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class SeparateAuthRequestClient {
    private static final String SUBJECT_TOKEN_TYPE = "urn:ietf:params:oauth:token-type:id_token";
    private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
    private static final String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
    private static final ObjectMapper OM = new ObjectMapper();

    private final JwtGenerator serviceJwtGen;
    private final RestTemplate restTemplate;

    @Value("${auth.service.kid}")
    private String serviceKid;
    @Value("${auth.service.tokenUri}")
    private String serviceTokenUri;
    @Value("${auth.service.clientId}")
    private String serviceClientId;

    @Value("${auth.provider.client-id}")
    private String providerClientId;
    @Value("${auth.provider.client-secret}")
    private String providerClientSecret;
    @Value("${auth.provider.token-uri}")
    private String providerTokenUri;
    @Value("${auth.provider.redirect-uri}")
    private String providerRedirectUri;

    public SeparateAuthRequestClient(JwtGenerator serviceJwtGen, RestTemplate restTemplate) {
        this.serviceJwtGen = serviceJwtGen;
        this.restTemplate = restTemplate;
        OM.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    public String getToken(String code) throws JsonProcessingException {
        String idToken = getIdToken(code);
        String jwt = serviceJwtGen.generateJwt(serviceClientId, serviceKid, serviceTokenUri);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", TOKEN_EXCHANGE_GRANT_TYPE);
        body.add("subject_token", idToken);
        body.add("subject_token_type", SUBJECT_TOKEN_TYPE);
        body.add("client_assertion_type", CLIENT_ASSERTION_TYPE);
        body.add("client_assertion", jwt);

        URI url = UriComponentsBuilder.fromUriString(serviceTokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, new LinkedMultiValueMap<>(), HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);
        TokenResponseModel tr = OM.readValue(res.getBody(), TokenResponseModel.class);

        return tr.accessToken;
    }

    private String getIdToken(String code) throws JsonProcessingException {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("client_id", providerClientId);
        body.add("client_secret", providerClientSecret);
        body.add("redirect_uri", providerRedirectUri);

        URI url = UriComponentsBuilder.fromUriString(providerTokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, new LinkedMultiValueMap<>(), HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);

        return OM.readValue(res.getBody(), TokenResponseModel.class).idToken;
    }
}
