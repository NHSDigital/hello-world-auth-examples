# User-restricted NHS login separate authentication and authorisation PHP tutorial

## Overview

This tutorial shows you how to connect to a [user-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#user-restricted-apis) using [NHS login separate authentication and authorisation](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/user-restricted-restful-apis-nhs-login-separate-authentication-and-authorisation)
and the PHP programming language. It uses [Symfony](https://symfony.com/) to create a simple web application which authenticates the end user using our sandbox NHS login environment, receives an access token from our authorisation server and calls the user restricted endpoint of our [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world).

To call a user-restricted API, the end user must be authenticated.
NHS login is used to authenticate when the end user is a patient. With the separate authentication and authorisation pattern, authentication is done by NHS login. In exchange, you receive an access ID token which you need to exchange it with an access token. You need to include this access token in the API request.

## Setting up your environment
This example project was developed using Symfony 6.1.4 and PHP 8.1.9 so you need to have these installed.

## Checkout the GitHub Repository

You can find the code for this PHP user-restricted REST API NHS login separate authentication and authorisation tutorial in
our [GitHub repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separate-auth-tutorials/nhs-login/php).

### Implementation details
This project contains:

- a `HelloAuthController` file. This contains the routes of the web application startup code and configures the OAuth flow required to authenticate. It uses the OAuth2 client provided by [league/oauth2-client](https://github.com/thephpleague/oauth2-client).

To follow this tutorial download or clone [this repository](https://github.com/NHSDigital/hello-world-auth-examples/tree/main/user-restricted-separate-auth-tutorials/nhs-login/php).

## Create an application on our developer portal

You need to create an application using our [Developer portal](https://digital.nhs.uk/developer/getting-started#create-a-developer-account). This gives you access to your application's API Key and Secret.

To do this, follow Step 1 'Create an application'
of [our guide](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication#step-1-create-an-application).

Notes:

- when creating a new app, you need to select the 'Environment'. For this tutorial select 'Sandbox'.
- when editing your application details and selecting the API you want to use, select 'Hello World (Sandbox)'.
- you are be prompted for a callback URL which is required for combined authentication and authorization. This URL is used to send users back to your application after successful (or unsuccessful) authorisation. This project is configured to run at `http://localhost:8000` and the OAuth handler creates as callback endpoint at `/hello`. Therefore the callback URL should be set to `http://localhost:8000/hello`.

- make note of your `API Key` and `API secret`.

## Populate the project's environment variables

You should now have your application's:

- `API Key`
- `API Secret`

To run the example tutorial, you need to set the following environment variables.

| Variable name         | Description                        |
| -----------           | ---------------------------------- |
| `CLIENT_ID`           | Your application's `API Key`       |
| `CLIENT_SECRET`       | Your application's `API Secret`    |

You can set your environment variables in a file named `.env`. This project contains a sample env file to use:

- rename `env.test` to `.env` and modify it.
- source it by running `source .env`

### Run the application

Set your environment variables in a file named `.env`. Then use the make command: `make run`.

## Using the application
When you run the code, you should be able to load the application at `http://localhost:8000`.
1. Click the button 'Login with NHS login' to be directed to our mock NHS login authorisation service
2. Sign in by selecting the highest NHS login [authentication level](https://nhsconnect.github.io/nhslogin/user-journeys/): P9.
3. You are be redirected back to the application. The access token you have received is used to make a request to the Hello World API.
4. The response from the API should read:

```
"Hello User!"
```