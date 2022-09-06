package nhsd.apim.auth;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class CustomTokenRequest implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
//        Assert.notNull(authorizationCodeGrantRequest, "authorizationCodeGrantRequest cannot be null");
//        RequestEntity<?> request = this.requestEntityConverter.convert(authorizationCodeGrantRequest);
        OAuth2AuthorizationExchange authorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add(OAuth2ParameterNames.GRANT_TYPE, "code");
        parameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());
        parameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
        parameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());

        String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
        if (redirectUri != null) {
            parameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        }
        URI uri = UriComponentsBuilder
                .fromUriString(authorizationGrantRequest.getClientRegistration().getProviderDetails().getTokenUri())
//                .queryParam(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode())
//                .queryParam(OAuth2ParameterNames.GRANT_TYPE, OAuth2ParameterNames.GRANT_TYPE, authorizationGrantRequest.getGrantType().getValue())
                .queryParams(parameters)

                .build().toUri();

        RequestEntity<OAuth2AccessTokenResponse> request = new RequestEntity<>(HttpMethod.POST, uri);
        ResponseEntity<OAuth2AccessTokenResponse> response = getResponse(request);
        OAuth2AccessTokenResponse tokenResponse = response.getBody();
        if (tokenResponse != null && CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
            // As per spec, in Section 5.1 Successful Access Token Response
            // https://tools.ietf.org/html/rfc6749#section-5.1
            // If AccessTokenResponse.scope is empty, then default to the scope
            // originally requested by the client in the Token Request
            // @formatter:off
            tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
                    .scopes(authorizationGrantRequest.getClientRegistration().getScopes())
                    .build();
            // @formatter:on
        }
        return tokenResponse;
    }

    private ResponseEntity<OAuth2AccessTokenResponse> getResponse(RequestEntity<?> request) {
        try {
            return this.restTemplate.exchange(request, OAuth2AccessTokenResponse.class);
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: "
                            + ex.getMessage(),
                    null);
            throw new OAuth2AuthorizationException(oauth2Error, ex);
        }
    }

//    void getstuff() {
//
//        ClientRegistration clientRegistration = authorizationCodeGrantRequest.getClientRegistration();
//        OAuth2AuthorizationExchange authorizationExchange = authorizationCodeGrantRequest.getAuthorizationExchange();
//        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
//        parameters.add(OAuth2ParameterNames.GRANT_TYPE, authorizationCodeGrantRequest.getGrantType().getValue());
//        parameters.add(OAuth2ParameterNames.CODE, authorizationExchange.getAuthorizationResponse().getCode());
//        String redirectUri = authorizationExchange.getAuthorizationRequest().getRedirectUri();
//        String codeVerifier = authorizationExchange.getAuthorizationRequest()
//                .getAttribute(PkceParameterNames.CODE_VERIFIER);
//        if (redirectUri != null) {
//            parameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
//        }
//        if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientRegistration.getClientAuthenticationMethod())
//                && !ClientAuthenticationMethod.BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
//            parameters.add(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
//        }
//        if (ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientRegistration.getClientAuthenticationMethod())
//                || ClientAuthenticationMethod.POST.equals(clientRegistration.getClientAuthenticationMethod())) {
//            parameters.add(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
//        }
//        if (codeVerifier != null) {
//            parameters.add(PkceParameterNames.CODE_VERIFIER, codeVerifier);
//        }
//    }
}
