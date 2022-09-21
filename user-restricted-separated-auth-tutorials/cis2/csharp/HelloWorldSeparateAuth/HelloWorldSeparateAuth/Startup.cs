using HelloWorldSeparateAuth.Configuration;
using HelloWorldSeparateAuth.JWT;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Authentication.OpenIdConnect;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;

namespace HelloWorldSeparateAuth;

public class Startup
{
    public Startup(IConfiguration configuration)
    {
        _configuration = configuration;
        _jwtHandler = new JwtHandler();
    }

    public IConfiguration _configuration { get;  }
    private readonly JwtHandler _jwtHandler;

    public void ConfigureServices(IServiceCollection services)
    {
        services.Configure<ApplicationConfigurations>
            (_configuration.GetSection("ApplicationConfigurations"));

        services.AddRazorPages();
        
        services.AddAuthentication(options => {
                options.DefaultScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultSignInScheme = CookieAuthenticationDefaults.AuthenticationScheme;
                options.DefaultChallengeScheme = OpenIdConnectDefaults.AuthenticationScheme;
            })
            .AddCookie(options => {
                options.Cookie.HttpOnly = true;
                options.Cookie.SameSite = SameSiteMode.Lax;
                options.Cookie.SecurePolicy = CookieSecurePolicy.Always;
                // The NHS Login ID token is only valid for 5 minutes
                options.ExpireTimeSpan = new System.TimeSpan(0, 5, 0);
            })
            .AddOpenIdConnect(options =>
                {
                    options.ClientId = _configuration["KEYCLOAK_CLIENT_ID"];
                    options.Authority = _configuration["KEYCLOAK_AUTHORITY"];
                    options.ResponseType = OpenIdConnectResponseType.Code;
                    options.GetClaimsFromUserInfoEndpoint = true;
                    options.SaveTokens = true;
                    options.Events = new OpenIdConnectEvents()
                    {
                        OnAuthorizationCodeReceived = context =>
                        {
                            context.TokenEndpointRequest.ClientAssertion = _jwtHandler.GenerateJwt(
                                _configuration["KEYCLOAK_CLIENT_ID"],
                                _configuration["KEYCLOAK_AUTHORITY"],
                                _configuration["KEYCLOAK_PRIVATE_KEY_PATH"],
                                _configuration["KID"]
                            );
                            context.TokenEndpointRequest.ClientAssertionType = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
                            return Task.CompletedTask;
                        }
                    };
                }
            );
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

    }
}