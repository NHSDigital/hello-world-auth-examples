# hello-world-auth-examples

# Overview
This repository shows you how to connect to an [application-restricted REST API](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation#application-restricted-apis) using [signed JWT authentication](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication) using the Java and C# programming language.

To call an application restricted API you need to tell the API who the calling application is. When using signed JWT authentication you need to authenticate your application by sending a JSON Web Token (JWT) to an authentication server, signed using your application's private key. In exchange you will receive an access token which you need to include in the API request. You can learn more about signed JWT authentication [here](https://digital.nhs.uk/developer/guides-and-documentation/security-and-authorisation/application-restricted-restful-apis-signed-jwt-authentication).

This tutorial will show how you can use Java or C# to generate and sign a JWT, exchange this for an access token with our authentication server and how to call our example [Hello World API](https://digital.nhs.uk/developer/api-catalogue/hello-world) using your access token.



### Java Application Restricted example

Follow [README](./application-restricted-signed-jwt-tutorials/java/README.md)

### C# Application Restricted example

Follow [README](./application-restricted-signed-jwt-tutorials/csharp/README.md)