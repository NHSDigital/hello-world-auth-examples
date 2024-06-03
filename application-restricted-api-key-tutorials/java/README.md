# Java Application-restricted REST API - API Key authentication

## Overview

This tutorial shows you how to connect to
an [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis)
using [API Key authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-api-key-authentication)
and the Java programming language.


## Setting up your environment

This example project was developed using Java 17 and maven 3.8.6.

## Checkout the GitHub Repository

You can find the code for this Java application-restricted REST API - API key authentication tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/application-restricted-api-key-tutorials/java).

This project contains:

- a `HelloWorld` class. The methods of this class make an application-restricted request to the API endpoint
- the `App` class. This contains the main entry point to run the program.

To follow this tutorial download or clone this folder.

## Create an application on our developer portal

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer). This gives you access to
your API key which you need to make a request.

## To create an application

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-api-key-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'. You might
  be prompted for a callback URL which is not required for the API Key authentication method, so you can enter a
  dummy value such as `https://www.example.com`.

- make note of your `API Key`.


## Populate the project's environment variables

You should now have:

- your application's `API Key`


To run the example tutorial, you need to set the following environment variables.

| Variable Name | Description                                                            |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `API_KEY`     | Your application's `API Key` |
| `ENDPOINT`    | The URL for the API you wish to call. In this tutorial, we make a request to the Hello World Sandbox's application-restricted endpoint: `https://sandbox.api.service.nhs.uk/hello-world/hello/application` |


You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.sample` to `.env` and modify it.
- source it by running `source .env`

Do not wrap your string variable values in quotes, as this can cause issues with maven interpreting the URL when compiling.

## Run the code

Once you set the environment variables, you are ready to run the project.

### Run using maven

Use the following commands to run the project using `maven` from the command line:

`mvn clean install` - This compiles, tests and packages your code.

`java -jar target/hello-world-auth-example-1.0-SNAPSHOT-jar-with-dependencies.jar` - This runs the executable jar file
produced in the previous step.

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.
When you run the code, you should receive the following response from the Hello World application:

```
{
  "message": "Hello Application!"
}
```
