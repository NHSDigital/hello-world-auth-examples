# C# Application-restricted REST API - API Key authentication

## Overview

This tutorial shows you how to connect to
an [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis)
using [API Key authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-api-key-authentication)
and the C# programming language.


## Setting up your environment

This example project was developed using .NET version 6.0.

## Checkout the GitHub Repository

You can find the code for this C# application-restricted REST API - API key authentication tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/application-restricted-api-key-tutorials/csharp).

This project contains:

- a `HelloWorld` class located in the `HelloWorld.cs` file. This class adds API key as a header and send a GET request to the specified endpoint

To follow this tutorial download or clone this folder.

## Create an application on our developer portal 

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer). This gives you access to
your application ID and API key which you need to generate a JWT.
You also need to create a public and private key pair. You register your public key with our authentication server and
sign your JWT using your private key.

## To create an application

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-api-key-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'. You might
  be prompted for a callback URL which is not required for the signed API Key authentication method, so you can enter a
  dummy value such as `https://www.example.com`.

- make note of your `API Key`.


## Populate the project's environment variables

You should now have:

- your application's `API Key`


To run the example tutorial, you need to set the following environment variables.

| Variable Name | Description                                                            |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `CLIENT_ID`    | Your application's `API Key`  
|  `ENDPOINT`    | The URL for the API you wish to call. In this tutorial, we make a request to the Hello World Sandbox's application-restricted endpoint: `https://sandbox.api.service.nhs.uk/hello-world/hello/application` | 


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
When you run the code, you should receive the following response from the Hello World application:

```
{
  "message": "Hello Application!"
}
```
