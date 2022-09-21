package nhsd.apim.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final CustomTokenRequest customTokenRequest;

    public WebSecurityConfig(CustomTokenRequest customTokenRequest) {
        this.customTokenRequest = customTokenRequest;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .addFilterBefore(new CustomAuth(), AuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/error", "/login/**", "/oauth2/authorization/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
//                                .exceptionHandling(e -> e
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                )
//                .oauth2Login();
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfo -> {
                                            userInfo.userService(authUserService());
                                            userInfo.oidcUserService(oidcUserService());
                                        }
                                )
//                                .authorizationEndpoint(authEndpoint -> authEndpoint())
//                                .tokenEndpoint(tokenEndpoint ->
//                                        tokenEndpoint.accessTokenResponseClient(customTokenRequest)))
                );
//                .oauth2Login();

        return http.build();

    }
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> authUserService() {
//        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
//            OidcUser oidcUser = delegate.loadUser(userRequest);
            OidcUser oidcUser = new DefaultOidcUser(null, null);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            // TODO
            // 1) Fetch the authority information from the protected resource using accessToken
            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities

            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());


            return oidcUser;
        };
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
//        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
//            OidcUser oidcUser = delegate.loadUser(userRequest);
            OidcUser oidcUser = new DefaultOidcUser(null, null);

            OAuth2AccessToken accessToken = userRequest.getAccessToken();
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            // TODO
            // 1) Fetch the authority information from the protected resource using accessToken
            // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities

            // 3) Create a copy of oidcUser but use the mappedAuthorities instead
            oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());


            return oidcUser;
        };
    }
}
