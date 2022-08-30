using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Authentication;


namespace HelloWorldCombinedAuth.Pages
{
    [Authorize]
    public class HelloWorldModel : PageModel
    {
        private readonly ILogger<IndexModel> _logger;
        private readonly IConfiguration _configuration;
        public String? ResponseCode { get; set; }
        public String? ResponseContent { get; set; }
        public String? APIEndpoint {get; set; }

        public HelloWorldModel(ILogger<IndexModel> logger, IConfiguration configuration)
        {
            _logger = logger;
            _configuration = configuration;
        }

        public async Task OnGet()
        {
            HttpClient client = new HttpClient();
            APIEndpoint = _configuration["NHSD:APIEndpoint"];

            var accessToken = await HttpContext.GetTokenAsync("access_token");
            var accessTokenString = (accessToken ?? "").ToString();

            client.DefaultRequestHeaders.Add("Authorization", "Bearer " + accessTokenString);

            HttpResponseMessage response = await client.GetAsync(APIEndpoint);
            ResponseCode = response.StatusCode.ToString();
            ResponseContent = await response.Content.ReadAsStringAsync();

        }

    }
}