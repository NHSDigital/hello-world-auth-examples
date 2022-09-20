using HelloWorldSeparateAuth.JWT;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication;


namespace HelloWorldCombinedAuth.Pages
{
    // Specifies access to this page requires authentication. Triggers specified oAuth flow if user not authenticated
    [Authorize]
    public class LoginModel : PageModel
    {
        private readonly IConfiguration _configuration;
        private readonly ILogger<IndexModel> _logger;
        private readonly JwtHandler _jwtHandler;
        public String? AccessToken { get; set; }
        public String? SessionExpires { get; set; }

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
            var values = new Dictionary<string, string>()
            {
                {"client_id", _configuration["CLIENT_ID"]},
                {"subject_token", idToken},
                {"client_assertion", clientAssertion},
                {"subject_token_type", "urn:ietf:params:oauth:token-type:id_token"},
                {"client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"},
                {"grant_type", "urn:ietf:params:oauth:grant-type:token-exchange"}
            };
            var content = new FormUrlEncodedContent(values);
            
            // var requestUri = new Uri(QueryHelpers.AddQueryString(tokenUrl, content));
            
            // Exchange the NHS Login ID token for an Access token
            var client = new HttpClient();
            var response = await client.PostAsync(tokenUrl, content);
            Console.WriteLine(response);

            // Fetch tokens that have been stored in the authentication cookie to display
            // var accessToken = await HttpContext.GetTokenAsync("access_token");
            // var tokenRefresh = await HttpContext.GetTokenAsync("refresh_token");
            // var tokenExpiresAt = await HttpContext.GetTokenAsync("expires_at");


            // AccessToken = (accessToken ?? "").ToString();
            // SessionExpires = Convert.ToDateTime(tokenExpiresAt).ToString("dd/MM/yy H:mm:ss");

        }

    }
}