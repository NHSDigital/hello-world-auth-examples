package nhsd.apim.controllers;

import nhsd.apim.hello.HelloWorld;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Objects;

@Controller
public class MainController {
    private final String ENDPOINT = System.getenv("ENDPOINT");

    @GetMapping("/home")
    public String index(Model model) {
        return "index"; //view
    }

    @GetMapping("/userinfo")
    public String[] userinfo(Model model) {
        /* Since NHS Login doesn't have the /userinfo endpoint exposed like on CIS2, we hardcode the attributes that
        Spring's OIDC discovery mechanism is looking for here to bypass the requirement for a /userinfo endpoint. */
        return new String[]{"name", "test"};
    }

    @GetMapping("/success")
    public String authSuccessful(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client, Model model) {
        OAuth2AccessToken accessToken = client.getAccessToken();
        String accessTokenString = accessToken.getTokenValue();
        String expiresIn = Objects.requireNonNull(accessToken.getExpiresAt()).toString();

        model.addAttribute("accessToken", accessTokenString);
        model.addAttribute("expiresIn", expiresIn);

        return "auth_successful"; //view
    }

    @GetMapping("/hello")
    public String helloWorldResponse(@RequestParam("accessToken") String accessToken, Model model) throws Exception {
        model.addAttribute("accessToken", accessToken);

        String[] helloWorldResponse = HelloWorld.makeUserRestrictedRequest(ENDPOINT, accessToken);

        String url = helloWorldResponse[0];
        String responseCode = helloWorldResponse[1];
        String responseBody = helloWorldResponse[2];

        model.addAttribute("url", url);
        model.addAttribute("responseCode", responseCode);
        model.addAttribute("responseBody", responseBody);

        return "hello_world"; //view
    }
}
