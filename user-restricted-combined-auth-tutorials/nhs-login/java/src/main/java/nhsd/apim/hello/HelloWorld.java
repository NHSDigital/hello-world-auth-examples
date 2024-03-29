package nhsd.apim.hello;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloWorld {

    public static String[] makeUserRestrictedRequest(final String helloEndpoint, final String accessToken) throws Exception {
        // Setup connection
        URL url = new URL(helloEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }

        String responseCode = String.valueOf(connection.getResponseCode());

        // Read response and return access token
        InputStream inputStream = connection.getInputStream();
        String streamText = new String(inputStream.readAllBytes());

        return new String[]{helloEndpoint, responseCode, streamText};
    }

}