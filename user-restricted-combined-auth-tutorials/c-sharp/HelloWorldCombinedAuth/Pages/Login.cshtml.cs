using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication;


namespace HelloWorldCombinedAuth.Pages
{
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
            var accessToken = await HttpContext.GetTokenAsync("access_token");
            var tokenRefresh = await HttpContext.GetTokenAsync("refresh_token");
            var tokenExpiresAt = await HttpContext.GetTokenAsync("expires_at");


            AccessToken = (accessToken ?? "").ToString();
            SessionExpires = Convert.ToDateTime(tokenExpiresAt).ToString("dd/MM/yy H:mm:ss");

        }

    }
}