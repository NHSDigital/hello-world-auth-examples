package nhsd.apim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

/**
 * Unit test for simple App.
 */

public class AppTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void mainShouldReturnHelloApplication() throws IOException, Exception {
        App.main(new String[] {});
        String expected = "{\n  \"message\": \"Hello Application!\"\n}";
        Assertions.assertEquals(expected, outputStreamCaptor.toString().trim());
    }
}
