package nhsd.apim.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.io.IOException;

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
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/error","/login/**", "/oauth2/authorization/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
//                                .exceptionHandling(e -> e
//                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                )
//                .oauth2Login();
                                .oauth2Login(oauth2Login ->
                        oauth2Login
//                                .authorizationEndpoint(authEndpoint -> authEndpoint.authorizationRequestResolver())
                                .tokenEndpoint(tokenEndpoint ->
                                tokenEndpoint.accessTokenResponseClient(customTokenRequest)))
        ;
//                .oauth2Login();

        return http.build();
    }
}
