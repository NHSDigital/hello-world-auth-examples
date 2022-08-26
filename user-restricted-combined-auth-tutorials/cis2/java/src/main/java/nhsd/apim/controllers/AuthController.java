package nhsd.apim;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.OutputStream;

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

    @GetMapping("/home")
    public String index(Model model) {
        return "index"; //view
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws Exception {
        // Build the URL parameters needed for authorization
        String authURL = Auth.authorize(OAUTH_ENDPOINT + "/authorize", CLIENT_ID, REDIRECT_URI);
        // Redirect to the external authorization endpoint and get the user to sign in with CIS2
        response.setHeader("Location", authURL);
        response.setStatus(302);
    }

    @GetMapping("/callback")
    public String authRedirect(@RequestParam("code") String code, Model model) throws Exception {
        model.addAttribute("code", code);
        return "login";
    }

    @GetMapping("/hello")
    public String helloWorld(Model model) {

        return "hello_world"; //view
    }

}
