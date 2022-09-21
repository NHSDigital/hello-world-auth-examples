package nhsd.apim.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
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

    private final JwtGenerator providerJwtGen;
    private final JwtGenerator serviceJwtGen;
    private final RestTemplate restTemplate;

    @Value("${auth.provider.kid}")
    private String providerKid;

    @Value("${auth.service.kid}")
    private String serviceKid;

    @Value("${auth.service.tokenUri}")
    private String serviceTokenUri;

    @Value("${auth.service.clientId}")
    private String serviceClientId;

    public SeparateAuthRequestClient(JwtGenerator providerJwtGen, JwtGenerator serviceJwtGen, RestTemplate restTemplate) {
        this.providerJwtGen = providerJwtGen;
        this.serviceJwtGen = serviceJwtGen;
        this.restTemplate = restTemplate;
        OM.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    public OAuth2AccessTokenResponse getToken(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) throws JsonProcessingException {
        String idToken = getIdToken(authorizationGrantRequest);
        String jwt = serviceJwtGen.generateJwt(serviceClientId, serviceKid, serviceTokenUri);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2ParameterNames.GRANT_TYPE, TOKEN_EXCHANGE_GRANT_TYPE);
        body.add("subject_token", idToken);
        body.add("subject_token_type", SUBJECT_TOKEN_TYPE);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION, jwt);

        URI url = UriComponentsBuilder.fromUriString(serviceTokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, new LinkedMultiValueMap<>(), HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);
        TokenResponseModel tr = OM.readValue(res.getBody(), TokenResponseModel.class);

        return tr.toOauth2AccessTokenResponse(idToken);
    }

    private String getIdToken(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) throws JsonProcessingException {
        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

        String tokenUri = authorizationGrantRequest.getClientRegistration().getProviderDetails().getTokenUri();
        String clientId = clientRegistration.getClientId();
        String code = authorizationExchange.getAuthorizationResponse().getCode();
        String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
        String jwt = providerJwtGen.generateJwt(clientId, providerKid, tokenUri);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2ParameterNames.GRANT_TYPE, "authorization_code");
        body.add(OAuth2ParameterNames.CODE, code);
        body.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        body.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE);
        body.add(OAuth2ParameterNames.CLIENT_ASSERTION, jwt);

        URI url = UriComponentsBuilder.fromUriString(tokenUri).build().toUri();
        RequestEntity<MultiValueMap<String, String>> request = new RequestEntity<>(body, new LinkedMultiValueMap<>(), HttpMethod.POST, url);
        ResponseEntity<String> res = restTemplate.exchange(request, String.class);

        return OM.readValue(res.getBody(), TokenResponseModel.class).idToken;
    }
}
