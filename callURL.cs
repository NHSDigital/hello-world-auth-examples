using System;
using System.Threading.Tasks;
using System.Net.Http;
using System.Net;
using System.IO;
using System.Net.Http.Headers;

// namespace hello_world_auth_examples
// {
// class callURL
// {
//     public static async Task<string> getApi2()
//         {
//             var url= "https://sandbox.api.service.nhs.uk/hello-world/hello/application";
//             HttpClient client = new HttpClient();
//             ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls;
//             client.DefaultRequestHeaders.Accept.Clear();
//             var response = client.GetStringAsync(url);
//             return await response;
//         }
// }
// }    

// using System;
// using System.Net;
// using System.Net.Http;
// using System.Net.Http.Headers;
// using System.Threading.Tasks;

namespace hello_world_auth_examples
{
class callURL
{
    //static readonly HttpClient client = new HttpClient();

    //public  async Task<string> getApi2()
    public async Task<string> getApi2()
    {
    try	
        {
            var url= "https://sandbox.api.service.nhs.uk/hello-world/hello/application";
            HttpClient client = new HttpClient();
            ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls;
            client.DefaultRequestHeaders.Accept.Clear();
            var response = client.GetStringAsync(url);
            return await response;
        }

    catch(HttpRequestException e)
        {
        Console.WriteLine("\nException Caught!");	
        Console.WriteLine("Message in callURL:{0} ",e.Message);
        Console.WriteLine("Inside catch");
        return e.Message;
        }    


    }
}
}


