package nhsd.apim.auth;

import java.time.Duration;
import java.util.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.MapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;

@Configuration
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
                    .accessTokenResponseClient(accessTokenResponseClient())
                .and().userInfoEndpoint()
                    .userService(this.oidcUserService());

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

    public static class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
        private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

        public CustomRequestEntityConverter() {
            defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
        }

        @Override
        public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
            RequestEntity<?> entity = defaultConverter.convert(req);
            assert entity != null;
            MultiValueMap<String, String> params = (MultiValueMap<String,String>) entity.getBody();

            assert params != null;
            params.add("client_id", System.getenv("CLIENT_ID"));
            params.add("client_secret", System.getenv("CLIENT_SECRET"));

            return new RequestEntity<>(params, entity.getHeaders(),
                    entity.getMethod(), entity.getUrl());
        }

    }

    public static class CustomTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {
        @Override
        public OAuth2AccessTokenResponse convert(Map<String, String> tokenResponseParameters) {
            MapOAuth2AccessTokenResponseConverter mapOAuth2AccessTokenResponseConverter = new MapOAuth2AccessTokenResponseConverter();
            OAuth2AccessTokenResponse original = mapOAuth2AccessTokenResponseConverter.convert(tokenResponseParameters);


            assert original != null;
            OAuth2AccessToken accessToken = original.getAccessToken();
            OAuth2RefreshToken refreshToken = original.getRefreshToken();
            assert refreshToken != null;

            return OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                    .tokenType(accessToken.getTokenType())
                    .scopes(accessToken.getScopes())
                    .expiresIn(Duration.ofMinutes(5).toSeconds())
                    .refreshToken(refreshToken.getTokenValue())
                    .additionalParameters(original.getAdditionalParameters())
                    .build();
        }
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oidcUserService() {
        return new CustomOAuth2UserService();
    }

    public static class CustomOAuth2UserService extends DefaultOAuth2UserService {
        /*
        We declare a custom user service here to get around Spring's need for a /userinfo endpoint to authenticate
        the user, we've already got an access token at this point but Spring wants an OAuth2User instance to
        connect to it if we want to restrict access to specific endpoints on our app.

        Since NHS Login doesn't have the /userinfo endpoint exposed like on CIS2, we hardcode the attributes that
        Spring's OIDC discovery mechanism is looking for, generate an authority, and give the name attribute that
        we already specified in application.yml.
        */
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
            var attributes = new HashMap<String, Object>();
            // Attribute values taken from a test user on Keycloak
            attributes.put("sub", "a76446b9-8fca-4bc3-b491-2fe148c2554f");
            attributes.put("email_verified", false);
            attributes.put("name", "User Test Mr");
            attributes.put("given_name", "Test");
            attributes.put("family_name", "User");

            Set<GrantedAuthority> authorities = new LinkedHashSet<>();
            authorities.add(new OAuth2UserAuthority(attributes));

            return new DefaultOAuth2User(authorities, attributes, "name");
        }
    }
}
