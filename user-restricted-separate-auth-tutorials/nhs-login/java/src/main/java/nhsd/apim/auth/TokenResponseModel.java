package nhsd.apim.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;

import java.util.Map;

/**
 * This object represent both ID Token response from OIDC and Access Token from service provider's Auth Server
 */
class TokenResponseModel {
    @JsonProperty("access_token")
    public String accessToken;
    @JsonProperty("id_token")
    public String idToken;
    @JsonProperty("refresh_token")
    public String refreshToken;
    @JsonProperty("issued_token_type")
    public String issuedTokenType;
    @JsonProperty("expires_in")
    public String expiresIn;
    @JsonProperty("refresh_expires_in")
    public String refreshExpiresIn;
    @JsonProperty("refresh_token_expires_in")
    public String refreshTokenExpiresIn;
    @JsonProperty("refresh_count")
    public String refreshCount;
    @JsonProperty("token_type")
    public String tokenType;
    @JsonProperty("not-before-policy")
    public String notBeforePolicy;
    @JsonProperty("session_state")
    public String sessionState;
    @JsonProperty("scope")
    public String scope;

//    OidcParameterNames.ID_TOKEN
    public OAuth2AccessTokenResponse toOauth2AccessTokenResponse(String idToken) {
        return OAuth2AccessTokenResponse
                .withToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(Long.parseLong(expiresIn))
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .additionalParameters(Map.of(OidcParameterNames.ID_TOKEN, idToken))
                .build();
    }

}
