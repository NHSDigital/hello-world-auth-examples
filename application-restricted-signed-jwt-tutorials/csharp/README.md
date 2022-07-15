# CSharp Application-restricted REST API - signed JWT authentication
## Overview
This example c# code shows how to access the [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis) with [signed JWT authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication).
## Setting up your environment
This example project was developed using c#
1. Install  .NET SDK 6.0 - see [Install .NET on Windows, Linux, and macOS](https://docs.microsoft.com/en-us/dotnet/core/install/)
2. Install an  IDE (Example: [Visual Studio Code](https://code.visualstudio.com/download)
## Checkout the Github Repository
All of our tutorials can be found in our [Github repository](https://github.com/NHSDigital/hello-world-auth-examples).
The code for this c# Application-restricted REST API - signed JWT authentication tutorial is found in this [folder] (https://github.com/NHSDigital/hello-world-auth-examples/tree/main/application-restricted-signed-jwt-tutorials/csharp).

To follow this tutorial download / clone this folder.
## Create an application on our developer hub and generate a key pair
You will need to create an application using our [Developer Hub](https://digital.nhs.uk/developer). This will give you access to your application ID and API key which are needed to generate a JWT.
You will also need to create a public and private key pair. You will register your public key with our authentication server and sign your JWT using your private key.

### To create an application
1. You will need to set up an account with [NHS Digital API Platform](https://digital.nhs.uk/developer)
2. Create an app on the NHS Digital API Platform and request access to Hello World - sandbox environment , registering your callback url https://digital.nhs.uk/api-spec-try-it-now/oauth-redirect
3. Make note of your `API Key `

### Generate a key pair
Generate a private key and public key by following the [NHS guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-2-generate-a-key-pair)

Make note of the `KID` you have chosen.

## Populate the project's environment variables
1. Open the c# application in any IDE and go to the folder csharp
2. Create an .env file with the following variables
	```
	export TOKEN_URL=<oauth2 token url>(example: https://sandbox.api.service.nhs.uk/oauth2/token)
	export KEY_FILE=<This is your pfx file/rsa private file>(example: jwtRS512.key , This is the private key generated from openssl)
	export CLIENT_ID=<application client id>(This is the API key  which was created from [NHS Digital API Platform](https://digital.nhs.uk/developer)
	export KID=<KID value associated with jwks>(This is the one which was given while generating a key pair, example: test-1)
	export ENDPOINT=<The /hello/application endpoint which for demonstrating application restricted APIs>(example: https://sandbox.api.service.nhs.uk/hello-world/hello/application)
	```

There is a sample environment file in the project that you can use to populate environment variables.
- Rename `env.sample` to `.env` and modify it accordingly.
- Source it by running `source .env`.

## Run the code
Once the environment variables have been set you are ready to run the project.

Use the following command to run this project from the command line:
```
  make run
```

You should see an output similar to this:
```shell
Received access token: 7Xuh7rK8iO8890mpk2b3BuFQuISG
Sending GET https://internal-dev.api.service.nhs.uk/hello-world/hello/application
Response from Hello World API:
{
  "message": "Hello Application!"
}

```
&nbsp;


### Implementation details
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
