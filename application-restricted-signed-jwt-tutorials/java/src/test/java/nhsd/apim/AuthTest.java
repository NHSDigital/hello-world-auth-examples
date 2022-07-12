package nhsd.apim;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AuthTest {
    @Test
    public void returnsValidAccessToken() throws IOException, Exception {
        String TOKEN_URL = System.getenv("TOKEN_URL");
        String PRIVATE_KEY_PATH = System.getenv("PRIVATE_KEY_PATH");
        String CLIENT_ID = System.getenv("CLIENT_ID");
        String KID = System.getenv("KID");

        String actual = Auth.getAccessToken(TOKEN_URL, CLIENT_ID, PRIVATE_KEY_PATH, KID);
        Assertions.assertNotNull(actual);
    }
}
