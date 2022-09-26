namespace HelloWorldSeparateAuth.Pages;

using HelloWorldSeparateAuth;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Authorization;

[Authorize]
public class HelloWorldModel : PageModel
{
    private readonly ILogger<HelloWorldModel> _logger;
    private readonly IConfiguration _configuration;
    public string? ResponseCode { get; set; }
    public string? ResponseContent { get; set; }
    public string? ApiEndpoint {get; set; }

    public HelloWorldModel(ILogger<HelloWorldModel> logger, IConfiguration configuration)
    {
        _logger = logger;
        _configuration = configuration;
    }

    public async Task OnGet()
    {
        ApiEndpoint = _configuration["ENDPOINT"];
        var client = new HttpClient();
        var accessToken = Config.AccessToken;
        client.DefaultRequestHeaders.Add("Authorization", "Bearer " + accessToken);

        var response = await client.GetAsync(ApiEndpoint);
        ResponseCode = response.StatusCode.ToString();
        ResponseContent = await response.Content.ReadAsStringAsync();

    }
}