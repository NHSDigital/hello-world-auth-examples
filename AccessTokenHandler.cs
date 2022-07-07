using System;
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

// This code also uses postAsync method without content-Type

namespace hello_world_auth_examples
{
    class AccessTokenHandler
    {
        
        //public string expires_in { get; set; }
        public string access_token { get; set; }
        private readonly string _jwt;

        public AccessTokenHandler(string jwt)
        {
         _jwt = jwt;
                 
        }

        public async Task<String>  getAccessToken()
        {
            
            var socketsHandler = new SocketsHttpHandler
                {
                    PooledConnectionLifetime = TimeSpan.FromMinutes(2)
                };

            using var client = new HttpClient(socketsHandler);    
           // using var client = new HttpClient();
            //client.DefaultRequestHeaders.Add("Accept", "application/x-www-form-urlencoded");
           // client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/x-www-form-urlencoded"));
            client.DefaultRequestHeaders.Accept.Clear();
            Console.WriteLine("inside access token") ;
            //client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/x-www-form-urlencoded"));
            var data = new[]
                {
                    new KeyValuePair<string, string>("grant_type", "client_credentials"),
                    new KeyValuePair<string, string>("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"),
                    new KeyValuePair<string, string>("client_assertion", _jwt)
                                                        
                }; 
            Console.WriteLine(data) ;   

            var url = "https://internal-dev.api.service.nhs.uk/oauth2/token";
            
            var response = await client.PostAsync(url, new FormUrlEncodedContent(data));

            Console.WriteLine(response);

            var result = await response.Content.ReadAsStringAsync();
            Console.Write(response.StatusCode.ToString());
            
            

            // Console.WriteLine(response.GetAwaiter().GetResult());

            Console.WriteLine(result);
            return(result);
            
            // HttpRequestMessage req = new HttpRequestMessage(HttpMethod.Post, url);
            // client.DefaultRequestHeaders.Clear();
            // client.DefaultRequestHeaders.Add("cache-control", "no-cache");
            // req.Content = new FormUrlEncodedContent(data);
            // req.Content.Headers.ContentType = new MediaTypeHeaderValue("application/x-www-form-urlencoded");
            // HttpResponseMessage tokenResponse = await client.SendAsync(req);
            // Console.WriteLine(string.Format("HttpResponseMessage.ReasonPhrase='{0}'", tokenResponse.ReasonPhrase));
            //return("abc");
        }

    }
}