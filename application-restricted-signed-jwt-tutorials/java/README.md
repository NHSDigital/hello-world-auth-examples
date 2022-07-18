# Java Application-restricted REST API - signed JWT authentication

## Overview
This tutorial shows you how to connect to an [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis) using [signed JWT authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication) using the Java programming language.

To call an application restricted API you need to tell the API who the calling application is. When using signed JWT authentication you need to authenticate your application by sending a JSON Web Token (JWT) to an authentication server, signed using your application's private key. In exchange you will receive an access token which you need to include in the API request. You can learn more about signed JWT authentication [here](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication).

This tutorial will show how you can use Java to generate and sign a JWT, exchange this for an access token with our authentication server and how to call our example [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world) using your access token.

## Setting up your environment
This example project was developed using Java 17 and maven 3.8.6.

## Checkout the Github Repository
The code for this Java Application-restricted REST API - signed JWT authentication tutorial is found in our [Github repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/application-restricted-signed-jwt-tutorials/java).

This project contains:
- An `Auth` class. The methods of this class handle the generation and signing of the JWT, and exchanging the JWT for an access token with the authentication server.
- A `Hello World` class. The methods of this class make an application restricted request to the API endpoint.
- The `App` class. This contains the main entry point to run the program. This program will get an access token using a signed JWT and use the access token to call the API.

To follow this tutorial download / clone this folder.

## Create an application on our developer hub and generate a key pair
You will need to create an application using our [Developer Hub](https://digital.nhs.uk/developer). This will give you access to your application ID and API key which are needed to generate a JWT.
You will also need to create a public and private key pair. You will register your public key with our authentication server and sign your JWT using your private key.

## To create an application
To do this follow Step 1 'Create an application' of our guide [here](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Note:

When creating a new app, you will need to select the 'Environment'. For this tutorial select 'Sandbox'.

When editing your application details and selecting the API you want to use select 'Hello World (Sandbox)'. You may be prompted for a callback URL. A callback URL is not required for the signed JWT authentication method so you may enter a dummy value such as `https://www.nowhere.com`.

Make note of your `API Key`.

### Generate a key pair
To do this: follow Step 2 'Generate a key pair' of our guide [here](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-2-generate-a-key-pair).

Make note of the KID you have chosen.

### Register your public key
To do this: follow Step 3 'Register your public key with us' of our guide [here](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-3-register-your-public-key-with-us).


## Populate the project's environment variables
You should now have
- your application's `API Key`
- a Key Identifier (KID) that you have chosen.
- your private key

To run the example tutorial you will need to set the following environment variables.
- `TOKEN_URL` This is the endpoint where you will send your signed JWT in order to exchange for an access token. For the Sandbox environment the value will be `https://sandbox.api.service.nhs.uk/oauth2/token`.
- `CLIENT_ID` This will be your application's `API Key`.
- `KID` This is the KID you chose when generating a public / private key pair.
- `PRIVATE_KEY_PATH` This will be a filepath pointing to where you have saved your private key.
- `ENDPOINT` This will be the URL for the API you wish to call. In this tutorial we will be making a request to the Hello World Sandbox's application restricted endpoint: `https://sandbox.api.service.nhs.uk/hello-world/hello/application`.

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:
- Rename `env.sample` to `.env` and modify it.
- Source it by running `source .env`

## Run the code
Once the environment variables have been set you are ready to run the project.

### Run using maven
Use the following commands to run this project using `maven` from the command line:

`mvn clean install` - This will compile, test and package your code.

`java -jar target/hello-world-auth-example-1.0-SNAPSHOT-jar-with-dependencies.jar` - This will run the executable jar that was produced in the previous step.

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command:  `make run`.

&nbsp;

**Upon running you should receive the following response from the Hello World application:**
```
{
  "message": "Hello Application!"
}

```