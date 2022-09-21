package nhsd.apim.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nhsd.apim.auth.SeparatedAuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

class TokenNotFound extends RuntimeException {
}

@RestController
public class AppController {
    private final static ObjectMapper OM = new ObjectMapper();

    private final SeparatedAuthenticationService separatedAuthenticationService;
    private final RestTemplate restTemplate;

    @Value("${app.endpoint}")
    private String endpoint;

    private String token;

    public AppController(SeparatedAuthenticationService separatedAuthenticationService, RestTemplate restTemplate) {
        this.separatedAuthenticationService = separatedAuthenticationService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hello/user")
    public String helloUser() {
        if (token == null) {
            throw new TokenNotFound(); // Trigger authorization. See tokenNotFoundHandler method.
        } else {
            RequestEntity<Void> req = RequestEntity.get(endpoint)
                    .header("Authorization", "Bearer " + this.token)
                    .build();
            ResponseEntity<String> res = restTemplate.exchange(req, String.class);
            return res.getBody();
        }
    }

    @ExceptionHandler(TokenNotFound.class)
    RedirectView tokenNotFoundHandler(final TokenNotFound e) {
        return new RedirectView(separatedAuthenticationService.redirect());
    }

    @GetMapping("/")
    public String index() {
        return "Visit hello/user for response from HelloWorld service";
    }

    @GetMapping("/callback")
    public RedirectView callback(@RequestParam(required = false) Map<String, String> queryParam) {
        String code = queryParam.get("code");
        this.token = separatedAuthenticationService.authenticate(code);

        return new RedirectView("/hello/user");
    }
}
