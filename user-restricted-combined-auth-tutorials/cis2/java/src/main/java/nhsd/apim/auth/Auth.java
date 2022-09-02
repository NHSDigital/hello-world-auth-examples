package nhsd.apim.auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Auth {

    public static JSONObject getTokenResponse(String tokenURL, String clientID, String clientSecret, String redirectURI, String code) throws Exception {
        // Set up params for /token request
        String urlParameters = String.join("&",
                "grant_type=authorization_code",
                "client_id=" + clientID,
                "client_secret=" + clientSecret,
                "redirect_uri=" + redirectURI,
                "code=" + code);

        // Create connection
        URL url = new URL(tokenURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");

        byte[] out = urlParameters.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = connection.getOutputStream();
        stream.write(out);

        int responseCode = connection.getResponseCode();
k
        if (responseCode != HttpURLConnection.HTTP_OK) {
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }

        // Read response and return access token
        InputStream inputStream = connection.getInputStream();
        String streamText = new String(inputStream.readAllBytes());
        JSONObject obj = new JSONObject(streamText);

        return obj;
    }
}