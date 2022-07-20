<?php

namespace php\auth;

require_once "JwtHandler.php";

class AuthClientCredentials
{
  private $tokenUrl;
  private $privateKeyFile;
  private $clientId;
  private $kid;

  function __construct($tokenUrl, $privateKeyFile, $clientId, $kid)
  {
    $this->tokenUrl = $tokenUrl;
    $this->privateKeyFile = $privateKeyFile;
    $this->clientId = $clientId;
    $this->kid = $kid;
  }

  public function AccessToken()
  {
    $jwtHandler = new JwtHandler($this->privateKeyFile, $this->tokenUrl, $this->clientId, $this->kid);
    $jwt = $jwtHandler->GenerateJwt();

    $headers = array(
        "Content-Type: application/x-www-form-urlencoded"
    );

    $data = array(
        "grant_type" => "client_credentials",
        "client_assertion_type" => "urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
        "client_assertion" => $jwt
    );

    $curl = curl_init($this->tokenUrl);
    curl_setopt($curl, CURLOPT_URL, $this->tokenUrl);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

    $resp = curl_exec($curl);
    curl_close($curl);
    $json = json_decode($resp);
    $accessToken = $json->access_token;
    return $accessToken;
  }
}
