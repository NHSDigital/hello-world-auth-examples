using System.Security.Claims;
using System.Text;
using HelloWorldSeparateAuth.Configuration;
using HelloWorldSeparateAuth.JWT;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;

namespace HelloWorldSeparateAuth;

public class Startup
{
    public Startup(IConfiguration configuration)
    {
        Configuration = configuration;
    }
    
    public IConfiguration Configuration { get;  }

    public void ConfigureServices(IServiceCollection services)
    {
        services.Configure<ApplicationConfigurations>
            (Configuration.GetSection("ApplicationConfigurations"));

        services.AddRazorPages();

        var key = new JwtHandler(Configuration, "").GenerateJwt();

        services.AddAuthentication(options =>
            {
                options.DefaultAuthenticateScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultSignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = "Keycloak";
            })
            .AddCookie(options =>
            {
                options.Cookie.HttpOnly = true;
                options.Cookie.SameSite = SameSiteMode.Lax;
                options.Cookie.SecurePolicy = CookieSecurePolicy.Always;
                options.ExpireTimeSpan = new System.TimeSpan(0, 10, 0);
                options.SlidingExpiration = true;
            })
            .AddOpenIdConnect("Keycloak", options =>
            {
                options.SignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.MetadataAddress =
                    "https://identity.ptl.api.platform.nhs.uk/auth/realms/NHS-Login-mock-sandbox/.well-known/openid-configuration";
                options.Authority = Configuration["KEYCLOAK_AUTHORITY"];
                options.SignedOutRedirectUri = Configuration["REDIRECT_URI"];
                options.ClientId = Configuration["KEYCLOAK_CLIENT_ID"];
                // options.ClientSecret = Configuration["KEYCLOAK_CLIENT_SECRET"];
                options.ResponseType = OpenIdConnectResponseType.Code;
                options.SaveTokens = true;
                options.GetClaimsFromUserInfoEndpoint = true;
                options.RequireHttpsMetadata = false;
                options.Scope.Add("openid");
                options.Scope.Add("profile");
                options.ClaimActions.Remove("aud");
                options.TokenValidationParameters.ValidateAudience = false;
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

        app.UseEndpoints(endpoints =>
        {
            endpoints.MapRazorPages();
        });
        
        // // Remove logging or other middleware from the favicon calls to reduce console clutter
        // app.Map("/favicon.ico", (app) => { });
        
    }
}