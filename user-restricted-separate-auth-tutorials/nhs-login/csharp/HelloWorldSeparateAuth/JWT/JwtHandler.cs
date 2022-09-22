using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;

namespace HelloWorldSeparateAuth.JWT;

public class JwtHandler
{
    public string GenerateJwt(string issuer, string audience, string keyPath, string kid)
    {
        var now = DateTime.UtcNow; 
        var token = new JwtSecurityToken(
            issuer,
            audience,
            new List<Claim>
            {
                new("sub", issuer),
                new("jti", Guid.NewGuid().ToString()),
                new("exp", (((DateTimeOffset)DateTime.Now).ToUnixTimeSeconds() + 300).ToString())
            },
            now,
            now.AddMinutes(5),
            FromPrivateKey(keyPath, kid)
        );
        var tokenHandler = new JwtSecurityTokenHandler();

        return tokenHandler.WriteToken(token);
    }
    
    private SigningCredentials FromPrivateKey(string privateKeyPath, string kid)
    {
        var privateKey = File.ReadAllText(privateKeyPath);
        privateKey = privateKey.Replace("-----BEGIN RSA PRIVATE KEY-----", "");
        privateKey = privateKey.Replace("-----END RSA PRIVATE KEY-----", "");
        var keyBytes = Convert.FromBase64String(privateKey);
        
        var rsa = RSA.Create();
        rsa.ImportRSAPrivateKey(keyBytes, out _);
        
        var rsaSecurityKey = new RsaSecurityKey(rsa)
        {
            KeyId = kid
        };

        return new SigningCredentials(rsaSecurityKey, SecurityAlgorithms.RsaSha512)
        {
            CryptoProviderFactory = new CryptoProviderFactory { CacheSignatureProviders = false }
        };
    }
    
}