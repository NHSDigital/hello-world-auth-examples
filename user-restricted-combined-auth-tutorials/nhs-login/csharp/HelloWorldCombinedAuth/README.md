# User-restricted NHS login combined authentication and authorisation C# tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS login combined authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-login-combined-authentication-and-authorisation)
and the C# programming language. It uses [Razor Pages in ASP.NET Core](https://docs.microsoft.com/en-us/aspnet/core/razor-pages) to create a simple web application which authenticates the end user using our sandbox NHS login environment, receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS login is used to authenticate when the end user is a patient. With the combined authentication and authorisation pattern, authentication is done by NHS login but is coordinated behind our OAuth2.0 authorisation server. In exchange, you receive an access token which you need to include in the API request.

## Setting up your environment
This example project was developed using .NET version 6.0.

## Checkout the GitHub Repository

You can find the code for this C# user-restricted REST API NHS login combined authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/nhs-login/csharp).

### Implementation details
This project contains:

- a `Program.cs` file. This contains the application startup code and configures the OAuth flow required to authenticate. It also configures cookie authentication, which means cookies are used to store whether the user is authenticated or not, and to store received access tokens.

- a `Pages` folder. This folder contains the Razor Pages. Pages marked with an `[Authorize]` tag show that the user must be authenticated (as determined by a cookie) to access them. If an unauthenticated user tries to access these pages it will trigger the OAuth authentication handler (configured in `Program.cs`) to run.

- a `HelloWorld` Razor page - located in the `Pages` folder. When this page is requested the stored access token is retrieved and used to send a GET request to the specified API endpoint.

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/nhs-login/csharp).

## Create an application on our developer portal

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer/getting-started#create-a-developer-account). This gives you access to your application's API Key and Secret.

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'.
- you will be prompted for a callback URL which is required for combined authentication and authorization. This URL is used to send users back to your application after successful (or unsuccessful) authorisation. This project is configured to run at `https://localhost:7114` and the OAuth handler creates as callback endpoint at `/callback`. Therefore the callback URL should be set to `https://localhost:7114/callback`.

- make note of your `API Key` and `API secret`.

## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables.

| Variable name         | Description                        |
| -----------           | ---------------------------------- |
| `NHSD__ClientId`      | Your application's `API Key`       |
| `NHSD__ClientSecret`  | Your application's `API Secret`    |

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.sample` to `.env` and modify it.
- source it by running `source .env`

## Run the code

Once you set the environment variables, you are ready to run the project.

### Run the application

You should first source your environment variable file before executing your application. Assuming you are using `dotnet` [cli tool](https://docs.microsoft.com/en-us/dotnet/core/tools/)
```shell
source .env
dotnet run
```

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.

## Using the application
When you run the code, you should be able to load the application at `https://localhost:7114`.
1. Click the button 'Login with NHS login' to be directed to our mock NHS login authorisation service
2. Sign in by selecting an NHS login [authentication level](https://nhsconnect.github.io/nhslogin/user-journeys/)
3. You will be redirected back to the application and the access token you have received will be displayed
4. To use the access token in an request to the Hello World API, click 'Call API'
5. The response from the API should read:

```
{
  "message": "Hello User!"
}
```