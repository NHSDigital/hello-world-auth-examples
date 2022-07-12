package nhsd.apim;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {
        String TOKEN_URL = System.getenv("TOKEN_URL");
        String PRIVATE_KEY_PATH = System.getenv("PRIVATE_KEY_PATH");
        String CLIENT_ID = System.getenv("CLIENT_ID");
        String KID = System.getenv("KID");
        String ENDPOINT = System.getenv("ENDPOINT");

        String accessToken = Auth.getAccessToken(TOKEN_URL, CLIENT_ID, PRIVATE_KEY_PATH, KID);

        String response = HelloWorld.makeApplicationRestrictedRequest(ENDPOINT, accessToken);
        System.out.println(response);
    }
}
