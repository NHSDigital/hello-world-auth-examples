This project demonstrates how to use oauth [client-credentials](https://oauth.net/2/grant-types/client-credentials/) grant type to perform authentication to use an application-restricted API.
It is **essential** to follow the official [NHS Digital guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication) first before running this project.

### Running the application
This project needs .net version 6.0. All the required parameters must be provided via environment variables. Here is a list of variables that you need:

```shell
export TOKEN_URL=<oauth2 token url>
export KEY_FILE=<This is the absolute path to your pfx file or private key file>
export CLIENT_ID=<application client id>
export KID=<KID value associated with jwks>

export ENDPOINT=<The /hello/application endpoint which for demonstrating application restricted APIs>
```
There is a sample environment file that you can use to populate environment variables. Rename `env.sample` to `.env` and modify it accordingly. Source it by running `source .env`. You can execute the
application by running `dotnet run`. Alternatively, you can use the provided Makefile. Make sure `.env` exists and then run `make run`.

You should see an output similar to this:
```shell
Received access token: 7Xuh7rK8iO8890mpk2b3BuFQuISG
Sending GET https://internal-dev.api.service.nhs.uk/hello-world/hello/application
Response from Hello World API:
{
  "message": "Hello Application!"
}
```

### Implementation 
There are two classes that handle authentication. `JwtHandler` generates the JWT and `AuthClientCredentials` sends the POST `/token` request to the auth server to get the access token. 
The `main` method sends a GET request to `/hello/application` endpoint of [hello-world](https://digital.nhs.uk/developer/api-catalogue/hello-world) API.

The `JwtHandler` class can sign a jwt using either private key or a pfx bundle. Given you have your private and public key, you can use `openssl` to create a pfx bundle. Below snippet shows how to do this using your certificate file (we create a self signed one here for demonstration).
```shell
openssl x509 -req -days 3650 -in certificate.csr -signkey jwtRS512.key -out certificate.crt
openssl pkcs12 -export -out jwtRS512.pfx -inkey jwtRS512.key -in certificate.crt
```

#### Dependencies
List of dependencies:

* Microsoft.AspNetCore.Authentication.JwtBearer
* IdentityModel
