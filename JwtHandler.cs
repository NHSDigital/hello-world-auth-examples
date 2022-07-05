using System;
using System.IO;
using System.Collections.Generic; 
using System.IdentityModel.Tokens.Jwt; 
using System.Security.Claims; 
using System.Security.Cryptography.X509Certificates; 
using IdentityModel; 
using Microsoft.IdentityModel.Tokens;
using System.Security.Cryptography;

namespace hello_world_auth_examples
{
public class JwtHandler
{    
     //private readonly RsaSecurityKey _cert;
     //private readonly SymmetricSecurityKey _cert;
     private readonly string _cert;
     private readonly string _audience;
     private readonly string _clientId;
     private readonly string _kid;
     
     

     public JwtHandler(string pfxCertPath, string audience, string clientId, string kid)
     {
         _audience = audience;
         _clientId = clientId;
         _kid = kid;
         _cert = pfxCertPath;
        
     }

     public string generateJWT(int expInMinutes = 1)
     {
        var privateKey = File.ReadAllText(_cert);
         Console.WriteLine(privateKey);
         
         privateKey = privateKey.Replace("-----BEGIN RSA PRIVATE KEY-----", "");
         privateKey = privateKey.Replace("-----END RSA PRIVATE KEY-----", "");
         Console.WriteLine(privateKey);
         byte[] keyBytes = Convert.FromBase64String(privateKey);
         using RSA rsa = RSA.Create();
         rsa.ImportRSAPrivateKey(keyBytes, out _);
        var rsaSecurityKey= new RsaSecurityKey(rsa);
        
        rsaSecurityKey.KeyId = _kid;
         var now = DateTime.UtcNow;
         var signingCredentials = new SigningCredentials(rsaSecurityKey, SecurityAlgorithms.RsaSha512)
              {
                  CryptoProviderFactory = new CryptoProviderFactory { CacheSignatureProviders = false }
              };
         

         var token = new JwtSecurityToken(
             _clientId,
             _audience,
             new List<Claim>
             {
                 //new ("jti", Guid.NewGuid().ToString()),
                 new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                 new (JwtClaimTypes.Subject, _clientId),
                 
             },
             now,
             now.AddMinutes(expInMinutes),
             signingCredentials: signingCredentials
             
              
         );
         var tokenHandler = new JwtSecurityTokenHandler();
         Console.WriteLine(tokenHandler.WriteToken(token));

         return tokenHandler.WriteToken(token);
     }

     
} 
}