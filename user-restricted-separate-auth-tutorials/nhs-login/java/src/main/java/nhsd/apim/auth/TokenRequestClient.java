package nhsd.apim.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

class TokenResponse {
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("expires_in")
    public String expiresIn;
    @JsonProperty("refresh_expires_in")
    public String refreshExpiresIn;
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("not-before-policy")
    public String notBeforePolicy;
    @JsonProperty("session_state")
    public String sessionState;
    @JsonProperty("scope")
    public String scope;
}

@Component
public class TokenRequestClient {
    private final JwtGenerator providerJwtGen;
    private final JwtGenerator serviceJwtGen;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${auth.provider.kid}")
    private String providerKid;

    @Value("${auth.service.kid}")
    private String serviceKid;

    @Value("${auth.service.tokenUri}")
    private String serviceTokenUri;

    @Value("${auth.service.clientId}")
    private String serviceClientId;

    public TokenRequestClient(JwtGenerator providerJwtGen, JwtGenerator serviceJwtGen, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.providerJwtGen = providerJwtGen;
        this.serviceJwtGen = serviceJwtGen;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public String getToken(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) throws JsonProcessingException {
        String idToken = getIdToken(authorizationGrantRequest);
        String jwt = serviceJwtGen.generateJwt(serviceClientId, serviceKid, serviceTokenUri);
        System.out.println("token exchange");
        System.out.println(jwt);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2ParameterNames.GRANT_TYPE, "urn:ietf:params:oauth:grant-type:token-exchange");
        body.add("subject_token", idToken);
        body.add("subject_token_type", "urn:ietf:params:oauth:token-type:id_token");
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION, jwt);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());

        URI url = UriComponentsBuilder.fromUriString(serviceTokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);

        return "";
    }

    private String getIdToken(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) throws JsonProcessingException {
        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

        String tokenUri = authorizationGrantRequest.getClientRegistration().getProviderDetails().getTokenUri();
        String clientId = clientRegistration.getClientId();
        // TODO: is it really needed? seems ok without it!
        String clientSecret = clientRegistration.getClientSecret();
        String code = authorizationExchange.getAuthorizationResponse().getCode();
        String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
        String clientAssertionType = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
        String jwt = providerJwtGen.generateJwt(clientId, providerKid, tokenUri);
        System.out.println("auth jwt:");
        System.out.println(jwt);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
        body.add(OAuth2ParameterNames.CODE, code);
//        body.add(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
        body.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        body.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, clientAssertionType);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION, jwt);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED.toString());

        URI url = UriComponentsBuilder.fromUriString(tokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, headers, HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        TokenResponse tr = om.readValue(res.getBody(), TokenResponse.class);
        System.out.println("access token:");
        System.out.println(tr.accessToken);
        return tr.accessToken;
    }


}
