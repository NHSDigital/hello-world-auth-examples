package nhsd.apim.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.MapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.RequestEntity;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/error", "/oauth2/authorization/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login()
                .tokenEndpoint()
                    .accessTokenResponseClient(accessTokenResponseClient());

    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        // Set a custom converter to handle /token requests
        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

        // Set a custom converter to handle the token response
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());
        // Assign this custom response converter with the RestTemplate
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        accessTokenResponseClient.setRestOperations(restTemplate);

        return accessTokenResponseClient;
    }

    public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
        private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

        public CustomRequestEntityConverter() {
            defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
        }

        @Override
        public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
            RequestEntity<?> entity = defaultConverter.convert(req);
            MultiValueMap<String, String> params = (MultiValueMap<String,String>) entity.getBody();
            // Generate a signed JWT for the client assertion
            // FIXME - get rid of this try/except
            String clientAssertion = null;
            try {
                clientAssertion = new JwtGenerator(System.getenv("PRIVATE_KEY_PATH"))
                        .generateJwt(
                                System.getenv("CLIENT_ID"),
                                System.getenv("KID"),
                                System.getenv("OAUTH_ENDPOINT")
                        );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            params.add("client_assertion", clientAssertion);
            params.add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
            return new RequestEntity<>(params, entity.getHeaders(),
                    entity.getMethod(), entity.getUrl());
        }

    }

    public class CustomTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {
        private static final Set<String> TOKEN_RESPONSE_PARAMETER_NAMES = Stream.of(
                OAuth2ParameterNames.ACCESS_TOKEN,
                OAuth2ParameterNames.TOKEN_TYPE,
                OAuth2ParameterNames.EXPIRES_IN,
                OAuth2ParameterNames.REFRESH_TOKEN,
                OAuth2ParameterNames.SCOPE).collect(Collectors.toSet());
        @Override
        public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
            MapOAuth2AccessTokenResponseConverter mapOAuth2AccessTokenResponseConverter = new MapOAuth2AccessTokenResponseConverter();
            OAuth2AccessTokenResponse original = mapOAuth2AccessTokenResponseConverter.convert(tokenResponseParameters);

            // To auth with NHS Login we need the ID token instead of the access token
            String idToken = tokenResponseParameters.get("id_token");
            OAuth2AccessToken accessToken = original.getAccessToken();

            return OAuth2AccessTokenResponse.withToken(idToken)
                    .tokenType(accessToken.getTokenType())
                    .scopes(accessToken.getScopes())
                    .expiresIn(Duration.ofMinutes(5).toSeconds())
                    .refreshToken(original.getRefreshToken().getTokenValue())
                    .additionalParameters(original.getAdditionalParameters())
                    .build();
        }

    }
}
