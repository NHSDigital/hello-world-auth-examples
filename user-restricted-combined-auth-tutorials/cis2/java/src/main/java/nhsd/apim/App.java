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
		System.out.println("User Restricted App.java");
		SpringApplication.run(App.class, args);
	}
}
