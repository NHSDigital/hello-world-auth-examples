using System;
using System.Threading.Tasks;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Net;
using System.Security.Cryptography;
using System.Text.RegularExpressions;
using Newtonsoft.Json;
using System.Net.Http;
using System.Net;
using System.IO;
using System.Net.Http.Headers;

namespace hello_world_auth_examples
{
    class Program
    {
        // static void Main(string[] args)
        static async Task Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            JwtHandler JwtHandler1 = new JwtHandler("jwtRS512.key","https://internal-dev.api.service.nhs.uk/oauth2/token","Too5BdPayTQACdw1AJK1rD4nKUD0Ag7J","test-1");
            var token1 = JwtHandler1.generateJWT(4);  //get JWT Token
           
            // var content = await res.Content.ReadAsStringAsync();
            // Console.WriteLine(content);
            // AccessTokenHandler AccObj = new AccessTokenHandler(token1);
            // var access_token1 = AccObj.getAccessToken();
            // callURL urlObj = new callURL();
            // var api = urlObj.getApi2();

             Bearer Bearer1 = new Bearer(token1);
             var access_token = Bearer1.getAccessToken();  // get access token
            // abc abc1 = new abc(token1);
            // Task<string> getAccessToken = abc1.GenerateAccessToken();  
            // string accessToken = await getAccessToken;    
           

            // var data = new[]
            //     {
            //         new KeyValuePair<string, string>("grant_type", "client_credentials"),
            //         new KeyValuePair<string, string>("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"),
            //         new KeyValuePair<string, string>("client_assertion", token1)
                                                        
            //     };    

            
            //  var url = "https://internal-dev.api.service.nhs.uk/oauth2/token";
            // // //var json;
            //  using var client = new HttpClient();
            //  client.DefaultRequestHeaders.Add("Accept", "application/json");
            //  var response = await client.PostAsync(url, new FormUrlEncodedContent(data));  
            // // var response = await client.PostAsync(url, new FormUrlEncodedContent(data)).GetAwaiter().GetResult();;
            
            // // var response =  response.ReadAsStringAsync().GetAwaiter().GetResult();
            // var result = await response.Content.ReadAsStringAsync();
            // //var res = response.Content;
            // //var result = res.ReadAsStringAsync().GetAwaiter().GetResult();
             
            
            // //var result =  response.Content.ReadAsString();
            //  Console.Write(response.StatusCode);
            //  Console.WriteLine(result);
            // // var url = "https://sandbox.api.service.nhs.uk/oauth2/token";
            // // using var client = new HttpClient();
            // // HttpRequestMessage req = new HttpRequestMessage(HttpMethod.Post, url);
            // // client.DefaultRequestHeaders.Clear();
            // // client.DefaultRequestHeaders.Add("cache-control", "no-cache");
            // // req.Content = new FormUrlEncodedContent(data);
            // // req.Content.Headers.ContentType = new MediaTypeHeaderValue("application/x-www-form-urlencoded");
            // // HttpResponseMessage tokenResponse = await client.SendAsync(req);
            // // Console.WriteLine(tokenResponse);
            // // Console.WriteLine(string.Format("HttpResponseMessage.ReasonPhrase='{0}'", tokenResponse.ReasonPhrase));
            // // if (sendtemp.IsSuccessStatusCode)
            // //     {
            // //         Console.WriteLine(sendtemp.Content.ReadAsStringAsync());
            // //     }
         //**********************************************************************

         // The below code uses SendAsync method to get the access token 
         //**********************************************************************

        //  using (var httpClient = new HttpClient())
        //  {
        //  using (var request = new HttpRequestMessage(new HttpMethod("POST"), "https://internal-dev.api.service.nhs.uk/oauth2/token"))
        //  {
        //      string strcont = "grant_type=client_credentials&"+"client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&"+"client_assertion="+token1;
        //      Console.WriteLine(strcont);
        //      request.Content = new StringContent(strcont);
        //      request.Content.Headers.ContentType = MediaTypeHeaderValue.Parse("application/x-www-form-urlencoded"); 
               
        //     request.Headers.Add("Authorization", $"Bearer {token1}");  

        //      var response = await httpClient.SendAsync(request);
        //      Console.WriteLine(response); 
        //  }

        // } 
          
            
        }

        

    }
}

