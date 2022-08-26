package nhsd.apim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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

    public static String buildAuthorizationURL(String authURL, String clientID, String redirectURI) throws Exception {
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
        HttpURLConnection connection = null;

        // Set up params for /token request
        String urlParameters = String.join("&",
                "grant_type=authorization_code",
                "client_id=" + clientID,
                "client_secret=" + clientSecret,
                "redirect_uri=" + redirectURI,
                "code=" + code);

        //Create connection
        URL url = new URL(tokenURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        //Send request
        DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.close();

        //Get Response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();

        // Read response and return access token
//        InputStream inputStream = connection.getInputStream();
//        String streamText = new String(inputStream.readAllBytes());
//        JSONObject obj = new JSONObject(streamText);
//        String accessToken = obj.getString("access_token");
//        return accessToken;
    }
}