package nhsd.apim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.security.KeyPair;
import java.security.PrivateKey;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.JSONObject;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Auth {
    public static String getAccessToken(String tokenEndpoint, String clientID, String privateKeyPath, String KID) throws Exception {
        // Setup connection
        URL url = new URL(tokenEndpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String jwt = generateJwt(clientID, privateKeyPath, KID, tokenEndpoint);

        // Set up params for token request
        String urlParameters = String.join("&",
                "grant_type=client_credentials",
                "client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                "client_assertion=" + jwt);
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

    private static String generateJwt(String clientID, String privateKeyPath, String KID, String tokenEndpoint) throws IOException {
        // Set expiry time now + 5 mins
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);
        Date expiryDate = now.getTime();

        PrivateKey privateKey = getPrivateKey(privateKeyPath);

        // Set header and payload claims. Sign with private key
        return buildJWT(clientID, tokenEndpoint, expiryDate, privateKey, KID);
    }

    private static String buildJWT(String clientID, String tokenEndpoint, Date expiryDate, PrivateKey privateKey, String KID) {
        String jwt = Jwts.builder()
                .setHeaderParam("alg", "RS512")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", KID)
                .setIssuer(clientID)
                .setSubject(clientID)
                .setAudience(tokenEndpoint)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
        return jwt;
    }

    private static PrivateKey getPrivateKey(String privateKeyPath) throws IOException {
        // Use Bouncy castle openssl library
        BufferedReader reader = new BufferedReader(new FileReader(privateKeyPath));
        PEMParser parser = new PEMParser(reader);
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        PrivateKey privateKey = keyPair.getPrivate();
        parser.close();
        return privateKey;
    }
}
