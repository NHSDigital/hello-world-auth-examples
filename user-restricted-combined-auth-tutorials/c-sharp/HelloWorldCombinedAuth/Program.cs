
using Microsoft.AspNetCore.WebUtilities;
using Microsoft.AspNetCore.Authentication.Cookies;

var builder = WebApplication.CreateBuilder(args);
var configuration = builder.Configuration;

// Add services to the container.
builder.Services.AddRazorPages();

builder.Services.AddAuthentication(options =>
{
    // Using cookie authentication scheme
    options.DefaultAuthenticateScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultSignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = "NHSD";

})
.AddCookie(options =>
{
    options.Cookie.HttpOnly = true;
    options.Cookie.SameSite = SameSiteMode.Lax;
    options.Cookie.SecurePolicy = CookieSecurePolicy.Always;
    options.ExpireTimeSpan = new System.TimeSpan(0, 10, 0);
    options.SlidingExpiration = true;
    options.Cookie.Name = "Tokens";
})
.AddOAuth("NHSD", options =>
{
    // The first oAuth endpoint - where user logs in
    var param = new Dictionary<string, string>() {
                {"response_type", "code"},
                {"client_id", configuration["NHSD:ClientId"]},
                {"redirect_uri", configuration["NHSD:CallbackUrl"]}
            } as IDictionary<string, string?>;
    var url = QueryHelpers.AddQueryString("/authorize", param);
    options.AuthorizationEndpoint = configuration["NHSD:OAuthEndpoint"] + url;

    // redirectUrl - middleware will create this endpoint
    options.CallbackPath = new PathString("/callback");

    // id and secret issued by oAUth provider
    options.ClientId = configuration["NHSD:ClientId"];
    options.ClientSecret = configuration["NHSD:ClientSecret"];

    // endpoint where we can exchange our auth code for an access token
    options.TokenEndpoint = configuration["NHSD:OAuthEndpoint"] + "/token";
    options.SaveTokens = true;
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthentication();
app.UseAuthorization();

app.MapRazorPages();

app.Run();
