package nhsd.apim.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {

    private String OAUTH_ENDPOINT = System.getenv("OAUTH_ENDPOINT");
    private String CLIENT_ID = System.getenv("CLIENT_ID");
    private String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
    private String REDIRECT_URI = System.getenv("REDIRECT_URI");
    private String ENDPOINT = System.getenv("ENDPOINT");

    @GetMapping("/error")
    public String error(HttpServletResponse response) {
        return "error";
    }

    @GetMapping("/home")
    public String index(Model model) {
        return "index"; //view
    }

    @GetMapping("/auth")
    public void authUser(HttpServletResponse response) throws Exception {
        // Build the URL parameters needed for authorization
        String authURL = Auth.buildAuthorizationURL(OAUTH_ENDPOINT + "/authorize", CLIENT_ID, REDIRECT_URI);
        // Redirect to the external authorization endpoint and get the user to sign in with CIS2
        response.setHeader("Location", authURL);
        response.setStatus(302);
    }

    @GetMapping("/callback")
    public void authRedirect(@RequestParam("code") String code, HttpServletResponse response, Model model) throws Exception {
        model.addAttribute("code", code);

        JSONObject tokenResponse = Auth.getTokenResponse(OAUTH_ENDPOINT + "/token", CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, code);
        String accessToken = tokenResponse.getString("access_token");
        String expiresIn = tokenResponse.getString("expires_in");

        String redirectParameters = String.join("&",
                "accessToken=" + accessToken,
                "expiresIn=" + expiresIn);
        String redirectURL = "/success?" + redirectParameters;

        response.setHeader("Location", redirectURL);
        response.setStatus(302);
    }

    @GetMapping("/success")
    public String authSuccessful(@RequestParam("accessToken") String accessToken, @RequestParam("expiresIn") String expiresIn, Model model) {
        model.addAttribute("accessToken", accessToken);
        model.addAttribute("expiresIn", expiresIn);
        return "login"; //view
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
