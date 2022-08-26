package nhsd.apim;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.stereotype.Component;

@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String [] ignoredPaths = new String[]{
                "/error", "/auth", "/callback", "/success", "/", "/home", "/pageNotFound",
                "/css/**", "/js/**", "/fonts/**", "/img/**"
        };

        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(configurer ->
                        configurer
                                .antMatchers(ignoredPaths)
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                // Enable JWT Authentication
                .oauth2ResourceServer().jwt();
    }
}