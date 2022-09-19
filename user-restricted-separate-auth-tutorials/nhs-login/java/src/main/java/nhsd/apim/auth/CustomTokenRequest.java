package nhsd.apim.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
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
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


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

class JwtGenerator {
    public void generateJwt() throws Exception {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey("");
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey("");
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
        }
    }

    public static PrivateKey getPrivateKey2(String filename)
            throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    public static PublicKey getPublicKey(String filename) throws Exception {

        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
    private static String generateJwt(String clientID, String privateKeyPath, String KID, String tokenEndpoint) throws IOException {
        // Set expiry time now + 5 mins
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);
        Date expiryDate = now.getTime();

        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        // Set header and payload claims. Sign with private key
        return buildJWT(clientID, tokenEndpoint, expiryDate, privateKey, KID);
    }

    private static String buildJWT(String clientID, String tokenEndpoint, Date expiryDate, PrivateKey privateKey, String KID) {
        return Jwts.builder()
                .setHeaderParam("alg", "RS512")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", KID)
                .setIssuer(clientID)
                .setSubject(clientID)
                .setAudience(tokenEndpoint)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
    }

    private static PrivateKey getPrivateKey(String privateKeyPath) throws IOException {
        // Use Bouncy castle openssl library
        BufferedReader reader = new BufferedReader(new FileReader(privateKeyPath));
        PEMParser parser = new PEMParser(reader);
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        PrivateKey privateKey = keyPair.getPrivate();
        parser.close();
        return privateKey;
    }

}
