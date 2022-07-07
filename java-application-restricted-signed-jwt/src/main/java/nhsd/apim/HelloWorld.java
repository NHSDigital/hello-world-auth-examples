package nhsd.apim;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloWorld {
    private static String BASE_URL = System.getenv("BASE_URL");

    public static String makeApplicationRestrictedRequest(String accessToken) throws IOException, Exception {
        // Setup connection
        URL url = new URL(BASE_URL + "/hello-world/hello/application");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer "+ accessToken);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response and return access token
            InputStream inputStream = connection.getInputStream();
            String streamText = new String(inputStream.readAllBytes());
            return streamText;
        } else {
            // Throw error
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }
    }

}