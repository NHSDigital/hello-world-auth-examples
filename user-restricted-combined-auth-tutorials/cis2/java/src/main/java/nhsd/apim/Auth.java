package nhsd.apim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Auth {

    public static String authorize(String authURL, String clientID, String redirectURI) throws Exception {
        // Setup connection
//        URL url = new URL(authURL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Set up params for /authorize request
        String urlParameters = String.join("&",
                "response_type=code",
                "client_id=" + clientID,
                "redirect_uri=" + redirectURI,
                "state=" + UUID.randomUUID().toString());
        String outputURL = authURL + "?" + urlParameters;

        return outputURL;
    }

    public static String getAccessToken(String tokenURL, String clientID, String clientSecret, String redirectURI, String code) throws Exception {
        // Setup connection
        URL url = new URL(tokenURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // Set up params for token request
        String urlParameters = String.join("&",
                "grant_type=authorization_code",
                "client_id=" + clientID,
                "client_secret=" + clientSecret,
                "redirect_uri=" + redirectURI,
                "code=" + code);
        OutputStream out = connection.getOutputStream();
        byte[] input = urlParameters.getBytes("utf-8");
        out.write(input);

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }

        // Read response and return access token
        InputStream inputStream = connection.getInputStream();
        String streamText = new String(inputStream.readAllBytes());
        JSONObject obj = new JSONObject(streamText);
        String accessToken = obj.getString("access_token");
        return accessToken;
    }
}