# User-restricted NHS Care Identity Service 2 separate authentication and authorisation Java tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS Care Identity Service 2 separate authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-cis2-separate-authentication-and-authorisation)
and the Java programming language. It uses [Spring](https://spring.io/quickstart) to create a simple web application which authenticates the end user using our [mock NHS Care Identity Service 2 authorisation service](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/testing-apis-with-our-mock-authorisation-service), receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS Care Identity Service 2 is used to authenticate when the end user is a healthcare worker. With the separate authentication and authorisation pattern, authentication is done by NHS Care Identity Service 2 (CIS2). In exchange, you receive an access ID token which you need to exchange it with an access token. You need to include this access token in the API request.

## Setting up your environment
This example project was developed using Java 17 and Maven 3.8.6.

## Checkout the GitHub Repository

You can find the code for this Java user-restricted REST API NHS Care Identity Service 2 separate authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separate-auth-tutorials/cis2/java).

### Implementation details
This project contains:

- an `App.java` file. This contains the application startup code

- a `controllers` folder containing a `MainController.java` file. This handles the authentication.

- a `auth` folder containing a set of classes that perform authentication, signing JWT and sending the token request.

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separate-auth-tutorials/cis2/java).

## To create an application

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'. You might
  be prompted for a callback URL which is not required for the signed JWT authentication method, so you can enter a
  dummy value such as `https://www.nowhere.com`.

- make note of your `API Key`.

### Generate a key pair

To do this, follow Step 2 'Generate a key pair'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-2-generate-a-key-pair).

Make a note of the Key Identifier (KID) you have chosen.

### Register your public key

To do this, follow Step 3 'Register your public key with us'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-3-register-your-public-key-with-us).
## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables. Variables with `PROVIDER_` prefix refers to the identity provider. In this tutorial we
use a mocked cis2 provider. There is already an application created for the hello-world tutorials so, you don't need to create one. In real production you must register your application with the 
required provider. All the required values for the mock provider is given to you and, you can find them in the `env.sample` file. The private key that you need to 
sign your JWT is also provided. In real application you should keep all these values as secrets and not include them in your project.
Variables with `SERVICE_` prefix refers to the application that you created in the NHS Digital portal i.e. previous steps.

| Variable name             | Description                                                                                                        |
|---------------------------|--------------------------------------------------------------------------------------------------------------------|
| `PROVIDER_CLIENT_ID`      | Your provider application's `client-id`                                                                            |
| `PROVIDER_CLIENT_SECRET`  | Your provider application's `client-secret`                                                                        |
| `PROVIDER_OAUTH_ENDPOINT` | Your oauth2 endpoint of the provider                                                                               |
| `PROVIDER_REDIRECT_URI`   | Your application's `Callback URL`. This is the URL that provider will use                                          |
| `ENDPOINT`                | Your application's `Environment URL` followed by `/hello-world/hello/user`                                         |
| `SERVICE_KEY_PATH`        | Absolute path to the private key file you created before when registering your application with NHS Digital portal |
| `SERVICE_CLIENT_ID`       | Your application client_id. This is the client-id of your NHS Digital app                                          |
| `SERVICE_OAUTH_ENDPOINT`  | Your service oauth endpoint.                                                                                       |

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.sample` to `.env` and modify it.
- source it by running `source .env`

## Run the code

Once you set the environment variables, you are ready to run the project.

### Run the application

You should first source your environment variable file before executing your application. Assuming you are using `maven` [cli tool](https://maven.apache.org/)
```shell
source .env
mvn spring-boot:run
```

#### Mock CIS2 app
This tutorial comes with a CIS2 mock provider. This application is called `hello-world-tutorials` and all the
required values are provided (see `env.sample` file). The default user is called `tutorialuser` and the password is the same as username.
Check `application.yml` file for other configuration options.
**NOTE:** Private key is part of source code for sample application. You should never store private keys in the repository.

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.

## Using the application
When you run the code, you should be able to load the application at `https://localhost:8080`
1. Enter this url: `http://localhost:8080/hello/user`
1. You will be redirected to the provider login page. Enter provided `tutorialuser` as username and the same as password
1. Upon successful authentication you will be redirected to the main page. You should the see response from hello-world service `hello/user`

```
{
  "message": "Hello User!"
}
```
