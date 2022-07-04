package nhsd.apim;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException, Exception {

        String accessToken = Auth.getAccessToken();
        System.out.println(accessToken);

        String response = HelloWorld.makeApplicationRestrictedRequest(accessToken);
        System.out.println(response);


    }
}
