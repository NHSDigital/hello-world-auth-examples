# Java Application-restricted REST API - signed JWT authentication

## Overview

This tutorial shows you how to connect to
an [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis)
using [signed JWT authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication)
and the Java programming language.

To call an application-restricted API, you need to tell the API which application is calling it. When using signed JWT
authentication you need to authenticate your application by sending a JSON Web Token (JWT) to an authentication server,
signed using your application's private key. In exchange, you receive an access token which you need to include in the
API request. To learn more about signed JWT authentication,
see [Application-restricted RESTful APIs - signed JWT authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication)
.

This tutorial shows how to use Java to generate and sign a JWT, exchange this for an access token with our
authentication server and call our example [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world)
using your access token.

## Setting up your environment

This example project was developed using Java 17 and maven 3.8.6.

## Checkout the Github Repository

You can find the code for this Java application-restricted REST API - signed JWT authentication tutorial in
our [Github repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/application-restricted-signed-jwt-tutorials/java).

This project contains:

- an `Auth` class. The methods of this class handle the generation and signing of the JWT, and exchanging the JWT for an
  access token with the authentication server
- a `Hello World` class. The methods of this class make an application-restricted request to the API endpoint
- the `App` class. This contains the main entry point to run the program. This program gets an access token using a
  signed JWT and uses the access token to call the API

To follow this tutorial download or clone this folder.

## Create an application on our developer hub and generate a key pair

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer). This gives you access to
your application ID and API key which you need to generate a JWT.
You also need to create a public and private key pair. You register your public key with our authentication server and
sign your JWT using your private key.

## To create an application

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application)
.

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'. You might
  be prompted for a callback URL which is not required for the signed JWT authentication method, so you can enter a
  dummy value such as `https://www.nowhere.com`.

- make note of your `API Key`.

### Generate a key pair

To do this, follow Step 2 'Generate a key pair'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-2-generate-a-key-pair)
.

Make a note of the Key Identifier (KID) you have chosen.

### Register your public key

To do this, follow Step 3 'Register your public key with us'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-3-register-your-public-key-with-us)
.

## Populate the project's environment variables

You should now have:

- your application's `API Key`
- a KID that you have chosen.
- your private key

To run the example tutorial, you need to set the following environment variables.

| Variable Name      | Description                                                                                                                                                                                                 |
|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `TOKEN_URL`        | The endpoint where you send your signed JWT in order to exchange for an access token. For the sandbox environment, the value is `https://sandbox.api.service.nhs.uk/oauth2/token`                           |
| `CLIENT_ID`        | Your application's `API Key`                                                                                                                                                                                |
| `KID`              | The KID you chose when generating a public/private key pair                                                                                                                                                 |
| `PRIVATE_KEY_PATH` | The filepath pointing to where you have saved your private key                                                                                                                                              |
| `ENDPOINT`         | The URL for the API you wish to call. In this tutorial, we make a request to the Hello World Sandbox's application-restricted endpoint: `https://sandbox.api.service.nhs.uk/hello-world/hello/application`  |

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.sample` to `.env` and modify it.
- source it by running `source .env`

## Run the code

Once you set the environment variables, you are ready to run the project.

### Run using maven

Use the following commands to run the project using `maven` from the command line:

`mvn clean install` - This compiles, tests and packages your code.

`java -jar target/hello-world-auth-example-1.0-SNAPSHOT-jar-with-dependencies.jar` - This runs the executable jar file
produced in the previous step.

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command:  `make run`.
When you run the code, you should receive the following response from the Hello World application:

```
{
  "message": "Hello Application!"
}
```
