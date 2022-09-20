package nhsd.apim.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class CustomTokenRequest implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {
    private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";
    private final RestTemplate restTemplate = new RestTemplate();

    private final TokenRequestClient tokenRequestClient;

    public CustomTokenRequest(TokenRequestClient tokenRequestClient) {
        this.tokenRequestClient = tokenRequestClient;
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        try {
            String t = tokenRequestClient.getToken(authorizationGrantRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        ResponseEntity<String> response = getResponse(request);
//        String tokenResponse = response.getBody();
//        if (tokenResponse != null && CollectionUtils.isEmpty(tokenResponse.getAccessToken().getScopes())) {
//            // As per spec, in Section 5.1 Successful Access Token Response
//            // https://tools.ietf.org/html/rfc6749#section-5.1
//            // If AccessTokenResponse.scope is empty, then default to the scope
//            // originally requested by the client in the Token Request
//            // @formatter:off
//            tokenResponse = OAuth2AccessTokenResponse.withResponse(tokenResponse)
//                    .scopes(authorizationGrantRequest.getClientRegistration().getScopes())
//                    .build();
//            // @formatter:on
//        }
        return null;
    }

    private ResponseEntity<String> getResponse(RequestEntity<?> request) {
        try {
            return this.restTemplate.exchange(request, String.class);
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

//class JwtGenerator2 {
//    public void generateJwt() throws Exception {
//        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey("");
//        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey("");
//        try {
//            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
//            String token = JWT.create()
//                    .withIssuer("auth0")
//                    .sign(algorithm);
//        } catch (JWTCreationException exception) {
//            //Invalid Signing configuration / Couldn't convert Claims.
//        }
//    }
//
//    public static PrivateKey getPrivateKey2(String filename)
//            throws Exception {
//
//        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
//
//        PKCS8EncodedKeySpec spec =
//                new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePrivate(spec);
//    }
//    public static PublicKey getPublicKey(String filename) throws Exception {
//
//        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
//
//        X509EncodedKeySpec spec =
//                new X509EncodedKeySpec(keyBytes);
//        KeyFactory kf = KeyFactory.getInstance("RSA");
//        return kf.generatePublic(spec);
//    }
//
//}
