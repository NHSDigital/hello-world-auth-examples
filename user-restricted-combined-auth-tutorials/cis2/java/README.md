# User-restricted NHS Care Identity Service 2 combined authentication and authorisation Java tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS Care Identity Service 2 (NHS CIS2) combined authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-cis2-combined-authentication-and-authorisation)
and the Java programming language. It uses [Spring](https://spring.io/quickstart) to create a simple web application which authenticates the end user using our [mock NHS CIS2 authorisation service](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/testing-apis-with-our-mock-authorisation-service), receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS CIS2 is used to authenticate when the end user is a healthcare worker. With the combined authentication and authorisation pattern, authentication is done by NHS CIS2 but is coordinated behind our OAuth2.0 authorisation server. In exchange, you receive an access token which you need to include in the API request.

## Setting up your environment
This example project was developed using Java 17 and Spring Boot 2.7.3.

## Checkout the GitHub Repository

You can find the code for this Java user-restricted REST API NHS CIS2 combined authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/cis2/java).

### Implementation details
This project contains:

- an `App.java` file. This contains the application startup code

- a `controllers` folder containing a `MainController.java` file. This handles each endpoint of the application, taking input data, performing operations if necessary, and passing the output to the template views.

- a `auth` folder containing a `WebSecurityConfig.java` file. This marks certain paths as restricted, requiring the user to authenticate to access them. If an unauthenticated user tries to access these pages it will trigger the OAuth authentication client (configured in `application.yml`) to run.

- a `hello` folder containing a `HelloWorld.java` file. This makes a GET request to the specified API endpoint using the provided access token.

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-combined-auth-tutorials/cis2/java).

## Create an application on our developer portal

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer/getting-started#create-a-developer-account). This gives you access to your application's API Key and Secret.

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'.
- you will be prompted for a callback URL which is required for combined authentication and authorization. This URL is used to send users back to your application after successful (or unsuccessful) authorisation. This project is configured to run at `https://localhost:8080` and Spring Security creates as callback endpoint at `/login/oauth2/code/simulated-auth`. Therefore the callback URL should be set to `https://localhost:8080/login/oauth2/code/simulated-auth`.

- make note of your `API Key` and `API secret`.

## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables.

| Variable name   | Description                                                                |
|-----------------|----------------------------------------------------------------------------|
| `CLIENT_ID`     | Your application's `API Key`                                               |
| `CLIENT_SECRET` | Your application's `API Secret`                                            |
| `OAUTH_ENDPOINT`| Your application's `Environment URL` followed by `/oauth2`                 |
| `REDIRECT_URI`  | Your application's `Callback URL`                                          |
| `ENDPOINT`      | Your application's `Environment URL` followed by `/hello-world/hello/user` |

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

### Run using Makefile
Alternatively you can set your environment variables in a file named `.env`. Then use the make command: `make run`.


## Using the application
When you run the code, you should be able to load the application at `https://localhost:8080`.
1. Click the button 'Login with NHS CIS2' to be directed to our mock NHS CIS2 authorisation service
2. Select an option to simulate a login and click 'Sign in'
3. You will be redirected back to the application and the access token you have received will be displayed
4. To use the access token in a request to the Hello World API, click 'Call API'
5. The response from the API should read:

```
{
  "message": "Hello User!"
}
```