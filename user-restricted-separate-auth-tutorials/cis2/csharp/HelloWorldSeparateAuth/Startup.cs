using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;

namespace HelloWorldSeparateAuth;

public class Startup
{
    public Startup(IConfiguration configuration)
    {
        CONFIGURATION = configuration;
    }

    private IConfiguration CONFIGURATION { get; }

    public void ConfigureServices(IServiceCollection services)
    {
        services.AddRazorPages();

        services.AddAuthentication(options =>
            {
                options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultSignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
            })
            .AddCookie(options =>
            {
                options.Cookie.HttpOnly = true;
                options.Cookie.SameSite = SameSiteMode.Lax;
                options.Cookie.SecurePolicy = CookieSecurePolicy.Always;
                // The ID token is only valid for 5 minutes
                options.ExpireTimeSpan = new TimeSpan(0, 5, 0);
            })
            .AddOpenIdConnect(options =>
            {
                options.ClientId = CONFIGURATION["KEYCLOAK_CLIENT_ID"];
                options.ClientSecret = CONFIGURATION["KEYCLOAK_CLIENT_SECRET"];
                options.Authority = CONFIGURATION["KEYCLOAK_AUTHORITY"];
                options.ResponseType = OpenIdConnectResponseType.Code;
                options.GetClaimsFromUserInfoEndpoint = true;
                options.SaveTokens = true;
            });
    }

    public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
    {
        if (env.IsDevelopment())
        {
            app.UseDeveloperExceptionPage();
        }

        app.UseHttpsRedirection();

        app.UseRouting();

        app.UseAuthentication();

        app.UseAuthorization();

        app.UseEndpoints(endpoints => { endpoints.MapRazorPages(); });

    }
}