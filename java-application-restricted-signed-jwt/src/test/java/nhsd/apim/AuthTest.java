package nhsd.apim;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AuthTest {
    @Test
    public void returnsValidAccessToken() throws IOException, Exception {
        String actual = Auth.getAccessToken();
        Assertions.assertNotNull(actual);
    }
}
