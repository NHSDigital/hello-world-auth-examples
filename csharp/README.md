### Running the application
Follow NHS Digital guide to create your application.

You need to provide these environment variables.
```shell
export TOKEN_URL=<oauth2 token url>
export KEY_FILE=<This is your pfx file>
export CLIENT_ID=<application client id>
export KID=<KID value associated with jwks>

export ENDPOINT=<The /hello/application endpoint which for demostrating application restriced APIs>
```
There is a sample env file that you can use. Rename `env.sample` to `.env` and modify it. Source it by running `source .env`. You can execute the
application by running `dotnet run`. Alternatively, you can use provide Makefile. Make sure `.env` exists and then run `make run`.

You should see an output similar to this:
```shell
Received access token: 7Xuh7rK8iO8890mpk2b3BuFQuISG
Sending GET https://internal-dev.api.service.nhs.uk/hello-world/hello/application
Response from Hello World API:
{
  "message": "Hello Application!"
}
```

```shell
dotnet add package Microsoft.AspNetCore.Authentication.JwtBearer --version 6.0.6
dotnet add package IdentityModel
```