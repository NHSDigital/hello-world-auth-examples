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
using System;
using System.IO;
using System.Net;
using System.Text;

// This the code to get the access token using webrequest GetRequestStream()(No Aysnc and await)

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
            string url = "https://internal-dev.api.service.nhs.uk/oauth2/token";
            string client_assertion_type = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"; 
            string client_assertion = _jwt;
            
           // byte[] byte1 = Encoding.ASCII.GetBytes("grant_type=client_credentials");
           


            //HttpWebRequest bearerReq = WebRequest.Create(url) as HttpWebRequest;
            WebRequest bearerReq = WebRequest.Create(url);
           // WebRequest request = WebRequest.Create("http://www.contoso.com/PostAccepter.aspx ");
           // bearerReq.Accept = "application/json";
            // var bearerReq = WebRequest.CreateHttp(url);
            // var response1 = bearerReq.GetResponse() as HttpWebResponse;
            // var uri = new Uri(response1.ResponseUri, "/oauth2/token");
            // bearerReq = WebRequest.CreateHttp(uri);
            bearerReq.Method = "POST";
            bearerReq.ContentType = "application/x-www-form-urlencoded";
            //bearerReq.ContentLength = byte1.Length;
            bearerReq.UseDefaultCredentials = true;
            bearerReq.PreAuthenticate = true;
            bearerReq.Credentials = CredentialCache.DefaultCredentials;
            
            
            //bearerReq.KeepAlive = false;
            //bearerReq.Headers.Add("Authorization", "Basic " + Convert.ToBase64String(Encoding.Default.GetBytes(client_assertion_type + ":" + client_assertion)));
            string postData = "grant_type=client_credentials&client_assertion_type=urn:ietf:params:oauth:client-assertion-type:jwt-bearer&client_assertion="+ _jwt;
            byte[] byteArray = Encoding.UTF8.GetBytes(postData);
            Console.WriteLine(byteArray);
            bearerReq.ContentLength = byteArray.Length;
            //Stream newStream = bearerReq.GetRequestStream();
            //newStream.Write(yteArray, 0, yteArray.Length);

            //WebResponse bearerResp = bearerReq.GetResponse();
            Stream dataStream = bearerReq.GetRequestStream();


            // Write the data to the request stream.
            dataStream.Write(byteArray, 0, byteArray.Length);
            // Close the Stream object.
            dataStream.Close();

            // Get the response.
            WebResponse bearerResp = bearerReq.GetResponse();
            
            // Display the status.
            Console.WriteLine(bearerResp);
            Console.WriteLine(((HttpWebResponse)bearerResp).StatusDescription);
            
            using (var reader = new StreamReader(bearerResp.GetResponseStream(), Encoding.UTF8))
            {
                var response = reader.ReadToEnd();
                Bearer bearer = JsonConvert.DeserializeObject<Bearer>(response);
                accessToken = bearer.access_token;
                Console.WriteLine(accessToken);
            }

            Console.WriteLine(accessToken);
            Console.Read();
            return(accessToken);
        }
    }
}