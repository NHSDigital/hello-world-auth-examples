using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography; 

namespace HelloWorldSeparateAuth.JWT;

public class JwtHandler
{
    private readonly IConfiguration _configuration;
    private readonly string _alg;
    // private readonly string _algEncrypt;
    private readonly SigningCredentials _signingCredentials;
    
    public JwtHandler(IConfiguration configuration, string alg)
    {
        _configuration = configuration;
        _alg = alg;
        // _algEncrypt = algEncrypt;
        _signingCredentials = FromPrivateKey(_configuration["PRIVATE_KEY_PATH"], _configuration["KID"]);
    }

    public string GenerateJwt()
    {
        var claims = new Dictionary<string, string>()
        {
            { "sub", _configuration["CLIENT_ID"] },
            { "iss", _configuration["CLIENT_ID"] },
            { "jti", Guid.NewGuid().ToString() },
            { "aud", _configuration["OAUTH_ENDPOINT"] + "/token" },
            { "exp", (((DateTimeOffset)DateTime.Now).ToUnixTimeSeconds() + 300).ToString() },
        };

        var additionalHeaders = new Dictionary<string, string>()
        {
            { "alg", _alg },
            { "typ", "JWT" },
            { "kid", _configuration["KID"] }
        };
        
        var now = DateTime.UtcNow; 
        var token = new JwtSecurityToken(
            _configuration["CLIENT_ID"],
            _configuration["OAUTH_ENDPOINT"] + "/token",
            new List<Claim>
            {
                new("sub", _configuration["CLIENT_ID"]),
                new("jti", Guid.NewGuid().ToString()),
                new("exp", (((DateTimeOffset)DateTime.Now).ToUnixTimeSeconds() + 300).ToString())
            },
            now,
            now.AddMinutes(5),
            _signingCredentials
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