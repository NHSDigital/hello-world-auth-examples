<?php

namespace App\Service;

class SeparateAuthHttpClient {

    private $code;
    private $jwt;
    private $tokenUrl;

  function __construct($code, $jwt, $tokenUrl)
  {
    $this->code = $code;
    $this->jwt = $jwt;
    $this->tokenUrl = $tokenUrl;
  }

  public function getAccessToken()
  {

    $headers = array(
        "Content-Type: application/x-www-form-urlencoded"
    );

    $data = array(
        "grant_type" => "authorization_code",
        "code" => $this->code,
        "redirect_uri" => 'http://localhost:8000/hello',
        'client_secret' => 'dde091bb-97af-483e-8d3e-02d6592cf318',
        'client_id' => 'hello-world-tutorials',
        'scope' => 'openid nationalrbacaccess'
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
    return $json;
  }
};
