namespace HelloWorldCombinedAuth.Pages;

using System.Text.Json.Nodes;
using HelloWorldSeparateAuth.JWT;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication;

// Specifies access to this page requires authentication. Triggers OIDC flow if user not authenticated
[Authorize]
public class LoginModel : PageModel
{
    private readonly IConfiguration _configuration;
    private readonly ILogger<IndexModel> _logger;
    private readonly JwtHandler _jwtHandler;
    public string? AccessToken { get; set; }
    public string? SessionExpires { get; set; }

    public LoginModel(IConfiguration configuration, ILogger<IndexModel> logger)
    {
        _configuration = configuration;
        _logger = logger;
        _jwtHandler = new JwtHandler();
    }

    public async Task OnGet()
    {
        // Grab the NHS Login ID token from the authorized response
        var idToken = await HttpContext.GetTokenAsync("id_token");
        var clientAssertion = _jwtHandler.GenerateJwt(
            _configuration["CLIENT_ID"], 
            _configuration["OAUTH_ENDPOINT"] + "/token", 
            _configuration["PRIVATE_KEY_PATH"],
            _configuration["KID"]);
        
        // Build the request URI parameters with the IDToken from Keycloak and the Client Assertion JWT
        var tokenUrl = _configuration["OAUTH_ENDPOINT"] + "/token";
        var values = new Dictionary<string, string?>()
        {
            {"client_id", _configuration["CLIENT_ID"]},
            {"subject_token", idToken},
            {"client_assertion", clientAssertion},
            {"subject_token_type", "urn:ietf:params:oauth:token-type:id_token"},
            {"client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"},
            {"grant_type", "urn:ietf:params:oauth:grant-type:token-exchange"}
        };
        var content = new FormUrlEncodedContent(values);
        
        // Exchange the NHS Login ID token for an Access token
        var client = new HttpClient();
        var response = await client.PostAsync(tokenUrl, content);
        var responseContent = await response.Content.ReadAsStringAsync();
        var parsedContent = JsonNode.Parse(responseContent);
        
        // Fetch tokens that have been stored in the authentication cookie to display
        var accessToken = parsedContent?["access_token"]?.ToString();
        var tokenExpiresIn = int.Parse(parsedContent?["expires_in"]?.ToString());

        AccessToken = accessToken;
        SessionExpires = DateTime.Now.AddSeconds(tokenExpiresIn).ToString("dd/MM/yy H:mm:ss");
    }
}