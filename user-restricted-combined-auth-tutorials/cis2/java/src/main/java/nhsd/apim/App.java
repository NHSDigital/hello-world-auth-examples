package nhsd.apim;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App
{
    public static void main(String[] args) throws IOException, Exception {
        String OAUTH_ENDPOINT = System.getenv("OAUTH_ENDPOINT");
        String CLIENT_ID = System.getenv("CLIENT_ID");
        String CLIENT_SECRET = System.getenv("CLIENT_SECRET");
        String REDIRECT_URI = System.getenv("REDIRECT_URI");
        String ENDPOINT = System.getenv("ENDPOINT");

        System.out.println("User Restricted App.java");

        SpringApplication.run(App.class, args);
    }
}
