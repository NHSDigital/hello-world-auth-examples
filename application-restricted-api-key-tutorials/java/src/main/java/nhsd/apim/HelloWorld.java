package nhsd.apim;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloWorld {

    public static String makeApplicationRestrictedRequest(String helloEndpoint, String APIKey) throws IOException, Exception {
        // Setup connection
        URL url = new URL(helloEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("apikey", APIKey);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }

        // Return response
        InputStream inputStream = connection.getInputStream();
        String streamText = new String(inputStream.readAllBytes());
        return streamText;
    }

}