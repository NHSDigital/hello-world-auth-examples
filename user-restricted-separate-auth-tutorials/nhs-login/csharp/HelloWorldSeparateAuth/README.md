# User-restricted Separate Authentication and Authorisation for NHS Login C# tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS login separate authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-login-separate-authentication-and-authorisation)
and the C# programming language. It uses [.NET Core](https://dotnet.microsoft.com/en-us/) to create a simple web application which authenticates the end user using our sandbox NHS login environment, receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS login is used to authenticate when the end user is a patient. With the separate authentication and authorisation pattern, authentication and authorisation are done separately. You might authenticate the user when they sign in but only get authorisation to call the API if and when you need it. You do authentication directly with NHS login and then separately do authorisation with our OAuth2.0 authorisation service.
## Setting up your environment
This example project was developed using .NET 6.0.102 so you need to have this installed.

## Checkout the GitHub Repository

You can find the code for this C# user-restricted REST API NHS login separate authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separate-auth-tutorials/nhs-login/csharp).

### Implementation details
This project contains:

- A `Program.cs` file that initialises the client application and creates a globally accessible `Config` class.
- A `Startup.cs` file that configures the authentication providers as well as other services.
- A `JWT/JwtHandler.cs` file that generates and signs JSON Web Tokens
- A `Pages` directory that contains each Razor Page used by the example client, each page contains its own html view and controller logic.   

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separated-auth-tutorials/nhs-login/csharp).

## Create an application on our developer portal

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer/getting-started#create-a-developer-account). This gives you access to your application's API Key and Secret.

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'.
- you are be prompted for a callback URL which is required for combined authentication and authorization. This URL is used to send users back to your application after successful (or unsuccessful) authorisation. This project is configured to run at `http://localhost:5001` and .NET's auth middleware creates a callback endpoint for the OIDC provider at `/signin-oidc`. Therefore the callback URL should be set to `http://localhost:5001/signin-oidc`.

- make note of your `API Key` and `API secret`.

## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables.

| Variable name               | Description                                                                                                                        |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| `KEYCLOAK_CLIENT_ID`        | `test-client-nhs-login`                                                                                                            |
| `KEYCLOAK_AUTHORITY`        | `https://identity.ptl.api.platform.nhs.uk/realms/NHS-Login-mock-sandbox`                                                      |
 | `KEYCLOAK_PRIVATE_KEY_PATH` | The path to the provider key. NHS login will provide this key, but for this tutorial you can use a our mock NHS login provider key | 
 | `OAUTH_ENDPOINT`            | `https://sandbox.api.service.nhs.uk/oauth2-mock`                                                                                   |
 | `ENDPOINT`                  | `https://sandbox.api.service.nhs.uk/hello-world/hello/user`                                                                        |
 | `CLIENT_ID`                 | Your application's `API Key`                                                                                                       |
 | `PRIVATE_KEY_PATH`          | The path to the private key generated for your `client_assertion`                                                                  |
 | `KID`                       | `test-1`                                                                                                                           |

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.sample` to `.env` and modify it.
- source it by running `source .env`

## Run the code

Once you set the environment variables, you are ready to run the project.

### Run the application

You should first source your environment variable file before executing your application.
```shell
source .env
dotnet run
```

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.

## Using the application
When you run the code, you should be able to load the application at `http://localhost:5001`.
1. Click the button 'Login with NHS login' to be directed to Keycloak.
2. Sign in with your user credentials for the client.
3. You are be redirected back to the application. The access token you have received is used to make a request to the Hello World API.
4. The response from the API should read:

```
"Hello User!"
```