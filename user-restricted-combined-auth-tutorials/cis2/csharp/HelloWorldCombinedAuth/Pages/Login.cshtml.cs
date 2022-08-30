using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication;


namespace HelloWorldCombinedAuth.Pages
{
    // Specifies access to this page requires authentication. Triggers specified oAuth flow if user not authenticated
    [Authorize]
    public class LoginModel : PageModel
    {
        private readonly ILogger<IndexModel> _logger;
        public String? AccessToken { get; set; }
        public String? SessionExpires { get; set; }

        public LoginModel(ILogger<IndexModel> logger)
        {
            _logger = logger;
        }

        public async Task OnGet()
        {
            // Fetch tokens that have been stored in the authentication cookie to display

            var accessToken = await HttpContext.GetTokenAsync("access_token");
            var tokenRefresh = await HttpContext.GetTokenAsync("refresh_token");
            var tokenExpiresAt = await HttpContext.GetTokenAsync("expires_at");


            AccessToken = (accessToken ?? "").ToString();
            SessionExpires = Convert.ToDateTime(tokenExpiresAt).ToString("dd/MM/yy H:mm:ss");

        }

    }
}