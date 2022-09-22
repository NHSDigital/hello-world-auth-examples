# syntax=docker/dockerfile:1
FROM mcr.microsoft.com/dotnet/sdk:6.0 AS base 
WORKDIR /app

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /app
RUN dotnet dev-certs https
COPY . ./
RUN dotnet restore
RUN dotnet build "HelloWorldSeparateAuth.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "HelloWorldSeparateAuth.csproj" -c Release -o /app/publish
    
# Build runtime image
FROM base AS final
WORKDIR /app
EXPOSE 5001
EXPOSE 80
ENV ASPNETCORE_URLS=http://+:80
COPY --from=publish /root/.dotnet/corefx/cryptography/x509stores/my/* /root/.dotnet/corefx/cryptography/x509stores/my/
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "HelloWorldSeparateAuth.dll"]

