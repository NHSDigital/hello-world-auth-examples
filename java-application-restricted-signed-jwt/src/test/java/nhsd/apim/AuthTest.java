package nhsd.apim;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AuthTest {
    @Test
    public void returnsValidAccessToken() throws IOException, Exception {
        String TOKEN_ENDPOINT = System.getenv("TOKEN_ENDPOINT");
        String CLIENT_ID = System.getenv("CLIENT_ID");
        String PRIVATE_KEY_PATH = System.getenv("PRIVATE_KEY_PATH");

        String actual = Auth.getAccessToken(TOKEN_ENDPOINT, CLIENT_ID, PRIVATE_KEY_PATH);
        Assertions.assertNotNull(actual);
    }
}
