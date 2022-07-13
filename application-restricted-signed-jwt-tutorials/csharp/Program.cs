using csharp.auth;

namespace csharp;

internal static class HelloWorld
{
    public static async Task Main(String[] args)
    {
        var tokenUrl = Environment.GetEnvironmentVariable("TOKEN_URL")!;
        var privateKeyFile = Environment.GetEnvironmentVariable("KEY_FILE")!;
        var clientId = Environment.GetEnvironmentVariable("CLIENT_ID")!;
        var kid = Environment.GetEnvironmentVariable("KID")!;
        
        var endpoint = Environment.GetEnvironmentVariable("ENDPOINT")!;

        var auth = new AuthClientCredentials(tokenUrl, privateKeyFile, clientId, kid);
        var accessToken = await auth.AccessToken() ?? throw new Exception("Failed to get access token");
        Console.WriteLine($"Received access token: {accessToken}");

        var response = await SendRequest(endpoint, accessToken);
        Console.WriteLine($"Response from Hello World API:\n{response}");
    }

    private static async Task<string> SendRequest(string url, string accessToken)
    {
        Console.WriteLine($"Sending GET {url}");

        var client = new HttpClient();
        client.DefaultRequestHeaders.Add("Authorization", $"Bearer {accessToken}");

        var response = await client.GetAsync(url);

        return await response.Content.ReadAsStringAsync();
    }
}