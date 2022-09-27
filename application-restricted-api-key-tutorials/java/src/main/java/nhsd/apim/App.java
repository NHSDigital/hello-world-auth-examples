package nhsd.apim.app-restricted;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        String ENDPOINT = System.getenv("ENDPOINT");
        String API_KEY = System.getenv("API_KEY");

        String response = HelloWorld.makeApplicationRestrictedRequest(ENDPOINT, API_KEY);
        System.out.println(response);
    }
}
