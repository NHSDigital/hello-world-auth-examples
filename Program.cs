using System;

namespace hello_world_auth_examples
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            JwtHandler JwtHandler1 = new JwtHandler("jwtRS512.key","https://sandbox.api.service.nhs.uk/oauth2/token","YylKQJ00Rdofb8XJx7GtDEeHKTg4fNy6","test-1");
            var token1 = JwtHandler1.generateJWT(5);
            
            Bearer Bearer1 = new Bearer(token1);
            var access_token = Bearer1.getAccessToken();
            //callApi1.getApi();
            // callApi callApi1 = new callApi();
            
            // response = await callApi1.getApi();
            // Console.WriteLine(response.GetType());
            callURL callURL1 = null;
            callURL1 = new callURL();
            var awaiter = callURL1.getApi2();
            if (awaiter.Result != "")
            {
                
                Console.WriteLine("HTML Response output to " + awaiter);
            }
            

            
        }
    }
}

