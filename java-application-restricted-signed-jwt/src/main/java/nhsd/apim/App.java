package nhsd.apim;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        String TOKEN_ENDPOINT = System.getenv("TOKEN_ENDPOINT");
        String HELLO_WORLD_ENDPOINT = System.getenv("HELLO_WORLD_ENDPOINT");
        String CLIENT_ID = System.getenv("CLIENT_ID");
        String PRIVATE_KEY_PATH = System.getenv("PRIVATE_KEY_PATH");

        String accessToken = Auth.getAccessToken(TOKEN_ENDPOINT, CLIENT_ID, PRIVATE_KEY_PATH);
        String response = HelloWorld.makeApplicationRestrictedRequest(HELLO_WORLD_ENDPOINT, accessToken);
        System.out.println(response);
    }
}
