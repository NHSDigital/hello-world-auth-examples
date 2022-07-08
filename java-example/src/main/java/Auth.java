import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Auth {
    private static String BASE_URL = "https://sandbox.api.service.nhs.uk/oauth2/token";
    private static String API_KEY = "LJpvkbAUHS6UENCLB3jkZUG7mF2mP7qm";

    public static String getAccessToken() throws Exception {
        // Setup connection
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String jwt = generateJwt();

        // Set up params for token request
        String urlParameters = String.join("&",
                "grant_type=client_credentials",
                "client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                "client_assertion=" + jwt);
        OutputStream out = connection.getOutputStream();
        byte[] input = urlParameters.getBytes("utf-8");
        out.write(input);


        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response and return access token
            InputStream inputStream = connection.getInputStream();
            String streamText = new String(inputStream.readAllBytes());
            JSONObject obj = new JSONObject(streamText);
            String accessToken = obj.getString("access_token");
            return accessToken;
        } else {
            // Throw error
            InputStream inputStream =  connection.getErrorStream();
            String streamText = new String(inputStream.readAllBytes());
            throw new Exception(streamText);
        }

    }

    private static String generateJwt() throws IOException {
        // Set expiry time now + 5 mins
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);
        Date expiryDate = now.getTime();

        PrivateKey privateKey = getPrivateKey();

        // Set header and payload claims. Sign with private key
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "RS512")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", "test-1")
                .setIssuer(API_KEY)
                .setSubject(API_KEY)
                .setAudience(BASE_URL)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
        return jwt;
    }

    private static PrivateKey getPrivateKey() throws IOException {
        // Use Bouncy castle openssl library
        BufferedReader reader = new BufferedReader(new FileReader("/Users/Andre.c.silva/IW/APIM/utils-snippets/key.txt"));
        PEMParser parser = new PEMParser(reader);
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        PrivateKey privateKey = keyPair.getPrivate();
        parser.close();
        return privateKey;
    }

}
