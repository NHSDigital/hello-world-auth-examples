package nhsd.apim.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private CustomTokenRequest customTokenRequest;

    public WebSecurityConfig(CustomTokenRequest customTokenRequest) {
        this.customTokenRequest = customTokenRequest;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/error", "/oauth2/authorization/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login();
//                ).oauth2Login(oauth2Login ->
//                        oauth2Login.tokenEndpoint(tokenEndpoint ->
//                                tokenEndpoint.accessTokenResponseClient(customTokenRequest)));

        return http.build();
    }
}
