### Running the application
Follow NHS Digital guide to create your application.

You need to provide these environment variables.
```shell
export TOKEN_URL=<oauth2 token url>
export PRIVATE_KEY_PATH=<This is your pfx file>
export CLIENT_ID=<application client id>
export KID=<KID value associated with jwks>
export ENDPOINT=<The /hello/application endpoint which for demostrating application restriced APIs>
```
There is a sample env file that you can use. Rename `env.sample` to `.env` and modify it. Source it by running `source .env`. You can execute the
application by running `mvn clean install` to build it and `java -jar target/hello-world-auth-example-1.0-SNAPSHOT-jar-with-dependencies.jar` to run it. Alternatively, you can use the provided Makefile. Make sure `.env` exists and then run `make run`.

You should see an output similar to this:
```shell
Received access token: 7Xuh7rK8iO8890mpk2b3BuFQuISG
Sending GET https://internal-dev.api.service.nhs.uk/hello-world/hello/application
Response from Hello World API:
{
  "message": "Hello Application!"
}
```
