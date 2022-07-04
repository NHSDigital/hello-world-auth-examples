using System;

namespace hello_world_auth_examples
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello World!");
            JwtHandler JwtHandler1 = new JwtHandler("jwtRS512.key","https://sandbox.api.service.nhs.uk/oauth2/token","YylKQJ00Rdofb8XJx7GtDEeHKTg4fNy6","test-1");
            var token1 = JwtHandler1.generateJWT(10);
            
        }
    }
}

