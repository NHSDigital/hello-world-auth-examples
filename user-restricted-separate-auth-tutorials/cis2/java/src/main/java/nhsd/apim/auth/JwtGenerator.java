package nhsd.apim.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class JwtGenerator {
    private final PrivateKey privateKey;

    public JwtGenerator(String privateKeyPath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(privateKeyPath));
        PEMParser parser = new PEMParser(reader);
        PEMKeyPair pemKeyPair = (PEMKeyPair) parser.readObject();
        KeyPair keyPair = new JcaPEMKeyConverter().getKeyPair(pemKeyPair);
        PrivateKey privateKey = keyPair.getPrivate();
        parser.close();

        this.privateKey = privateKey;
    }

    public String generateJwt(String clientID, String kid, String aud) {
        // Set expiry time now + 5 mins
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 5);
        Date expiryDate = now.getTime();

        return Jwts.builder()
                .setHeaderParam("alg", "RS512")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", kid)
                .setIssuer(clientID)
                .setSubject(clientID)
                .setAudience(aud)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiryDate)
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();
    }

}
