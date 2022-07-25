using System.Net;

namespace csharp;

internal static class HelloWorld
{
    public static async Task Main(String[] args)
    {
        
        var clientId = Environment.GetEnvironmentVariable("CLIENT_ID")!;
        var endpoint = Environment.GetEnvironmentVariable("ENDPOINT")!;

        var response = await SendRequest(endpoint, clientId);
        Console.WriteLine($"Response from Hello World API:\n{response}");
    }

    private static async Task<string> SendRequest(string url, string clientId)
    {
        Console.WriteLine($"Sending GET {url}");

        var client = new HttpClient();
        client.DefaultRequestHeaders.Add("apikey", $"{clientId}");
        
        var response = await client.GetAsync(url);

        return await response.Content.ReadAsStringAsync();
        
    }
}