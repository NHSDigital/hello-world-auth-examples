package nhsd.apim.authcontroller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class AuthController {

    // inject via application.properties
    @Value("${welcome.message}")
    private String message;

    private List<String> tasks = Arrays.asList("a", "b", "c", "d", "e", "f", "g");

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

        return "index"; //view
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", message);
        model.addAttribute("tasks", tasks);

        return "login"; //view
    }

    // /hello?name=kotlin
    @GetMapping("/hello")
    public String helloWorld(Model model) {

        return "hello_world"; //view
    }

}
