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

// This code uses postSync  method to get the access token


namespace hello_world_auth_examples
{
class abc
{    
        private readonly string _jwt;

        public abc(string jwt)
        {
         _jwt = jwt;
                 
        }

    public async Task<string> GenerateAccessToken()  
            {  
                //AccessTokenResponse token = null;  
    
                try  
                {  
                    Console.WriteLine("Inside the accesstoken");
                    HttpClient client = new HttpClient();  
                    string body = "grant_type=client_credentials&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&client_assertion:"+ _jwt;  
                    client.BaseAddress = new Uri("https://internal-dev.api.service.nhs.uk/oauth2/token");  
                    client.DefaultRequestHeaders.Accept.Clear();  
                    client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/x-www-form-urlencoded")); 
                    HttpRequestMessage request = new HttpRequestMessage(HttpMethod.Post, client.BaseAddress);  
                    request.Content = new StringContent(body, Encoding.UTF8, "application/x-www-form-urlencoded");
                     //CONTENT-TYPE header  
                    var baseUrl = "https://internal-dev.api.service.nhs.uk/oauth2/token" ;                                   
                                                        
    
                    // List<KeyValuePair<string, string>> postData = new List<KeyValuePair<string, string>>();  
    
                    // postData.Add(new KeyValuePair<string, string>("grant_type", "client_credentials"));  
                    // postData.Add(new KeyValuePair<string, string>("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
                    // postData.Add(new KeyValuePair<string, string>("client_assertion", token1)); 
                    var postData = new[]
                    {
                        new KeyValuePair<string, string>("grant_type", "client_credentials"),
                        new KeyValuePair<string, string>("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"),
                        new KeyValuePair<string, string>("client_assertion", _jwt)
                                                            
                    };   
    
                    request.Content = new FormUrlEncodedContent(postData);  
                    
                    HttpResponseMessage tokenResponse = client.PostAsync(baseUrl, new FormUrlEncodedContent(postData)).Result;
                    //HttpResponseMessage tokenResponse = client.SendAsync(baseUrl).Result;
                    //var token = tokenResponse.Content.ReadAsStringAsync().Result;    
                    //token = await tokenResponse.Content.ReadAsAsync<AccessTokenResponse>(new[] { new JsonMediaTypeFormatter() });  
                    var token = await tokenResponse.Content.ReadAsStringAsync();
                    Console.WriteLine(token);
                    return(token);

                }  
    
    
                catch (HttpRequestException ex)  
                {  
                    throw ex;  

                }  
               // return token != null ;  
    
            }  

}    
}
