# User-restricted NHS login combined authentication and authorisation Java tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS login combined authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-login-combined-authentication-and-authorisation)
and the Java programming language. It uses [Spring](https://spring.io/quickstart) to create a simple web application which authenticates the end user using our [mock NHS login authorisation service](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/testing-apis-with-our-mock-authorisation-service), receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS login is used to authenticate when the end user is a patient. With the separate authentication and authorisation pattern, authentication is done by NHS login. In exchange, you receive an access ID token which you need to exchange it with an access token. You need to include this access token in the API request.

## Setting up your environment
This example project was developed using Java 17 and Maven 3.8.6.

## Checkout the GitHub Repository

You can find the code for this Java user-restricted REST API NHS Login separate authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/nhs-login/java).

### Implementation details
This project contains:

- an `App.java` file. This contains the application startup code

- a `controllers` folder containing a `MainController.java` file. This handles the authentication.

- an `auth` folder containing a class to configure which endpoints are user-restricted by Spring, as well as handle /token and /userinfo requests.

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/nhs-login/java).

## To create an application

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'. 
- you will be prompted for a callback URL which is required for combined authentication and authorization. This URL is used to send users back to your application after successful (or unsuccessful) authorisation. This project is configured to run at https://localhost:8080 and Spring creates as callback endpoint at /login/oauth2/code/keycloak. Therefore the callback URL should be set to http://localhost:8080/login/oauth2/code/keycloak.
- make note of your `API Key` and `API Secret`.

## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables.

| Variable name       | Description                                                                |
|---------------------|----------------------------------------------------------------------------|
| `CLIENT_ID`         | Your application's `API Key`                                               |
| `CLIENT_SECRET`     | Your application's `API Secret`                                            |
| `OAUTH_ENDPOINT`    | Your application's `Environment URL` followed by `/oauth2-mock`            |
| `REDIRECT_URI`      | Your application's `Callback URL`                                          |
| `ENDPOINT`          | Your application's `Environment URL` followed by `/hello-world/hello/user` |

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

#### Mock NHS login app
This tutorial comes with an NHS login mock provider. This application is called `hello-world-tutorials`. 
The default user is called `tutorialuser` and the password is the same as username.
Check `application.yml` file for other configuration options.

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.

## Using the application
When you run the code, you should be able to load the application at `https://localhost:8080`.
1. Click the button 'Login with NHS LOGIN' to be directed to our mock NHS LOGIN authorisation service
2. Select an option to simulate a login and click 'Sign in'
3. You will be redirected back to the application and the access token you have received will be displayed
4. To use the access token in a request to the Hello World API, click 'Call API'
5. The response from the API should read:

```
{
  "message": "Hello User!"
}
```
