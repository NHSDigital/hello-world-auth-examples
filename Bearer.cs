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


namespace hello_world_auth_examples
{
    public class Bearer
    {
        
        //public string expires_in { get; set; }
        public string access_token { get; set; }
        private readonly string _jwt;

        public Bearer(string jwt)
        {
         _jwt = jwt;
                 
        }

        public string getAccessToken()
        {
            
            string accessToken;
            string url = "https://api.service.nhs.uk/oauth2/token";
            string client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"; 
            string client_assertion = _jwt;
            
            byte[] byte1 = Encoding.ASCII.GetBytes("grant_type=client_credentials");

            HttpWebRequest bearerReq = WebRequest.Create(url) as HttpWebRequest;
            bearerReq.Accept = "application/json";
            bearerReq.Method = "POST";
            bearerReq.ContentType = "application/x-www-form-urlencoded";
            bearerReq.ContentLength = byte1.Length;
            bearerReq.KeepAlive = false;
            bearerReq.Headers.Add("Authorization", "Basic " + Convert.ToBase64String(Encoding.Default.GetBytes(client_assertion_type + ":" + client_assertion)));
            Stream newStream = bearerReq.GetRequestStream();
            newStream.Write(byte1, 0, byte1.Length);

            WebResponse bearerResp = bearerReq.GetResponse();
            
            using (var reader = new StreamReader(bearerResp.GetResponseStream(), Encoding.UTF8))
            {
                var response = reader.ReadToEnd();
                Bearer bearer = JsonConvert.DeserializeObject<Bearer>(response);
                accessToken = bearer.access_token;
            }

            Console.WriteLine(accessToken);
            Console.Read();
            return(accessToken);
        }
    }
}