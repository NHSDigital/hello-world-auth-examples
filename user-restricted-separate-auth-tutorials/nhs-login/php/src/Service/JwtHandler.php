<?php

namespace App\Service;

class JwtHandler
{

  
  private $audience;
  private $client_id;
  private $kid;
  private $privateKeyFile;
  private $alg;

  function __construct($privateKeyFile, $audience, $client_id, $kid, $alg, $alg_encrypt)
  {
    $this->audience = $audience;
    $this->client_id = $client_id;
    $this->kid = $kid;
    $this->privateKeyFile = $privateKeyFile;
    $this->alg = $alg;
    $this->alg_encrypt = $alg_encrypt;
  }

  public function GenerateJwt()
  {
    $claims = [
        "sub" => $this->client_id,
        "iss" => $this->client_id,
        "jti" => uniqid(),
        "aud" => $this->audience,
        "exp" => (time()) + 300 # 5mins in the future
    ];
    $payload_encoded = $this->base64UrlEncode(json_encode($claims));

    $additional_headers = [
        "alg" => $this->alg,
        "typ" => "JWT",
        "kid" => $this->kid
    ];
    $headers_encoded = $this->base64UrlEncode(json_encode($additional_headers));

    $fp = fopen($this->privateKeyFile, "r");
    $privateKey = fread($fp, 8192);
    openssl_sign("$headers_encoded.$payload_encoded", $signature, $privateKey, $this->alg_encrypt);
    $signature_encoded = $this->base64UrlEncode($signature);

    $jwt = "$headers_encoded.$payload_encoded.$signature_encoded";
    return $jwt;
  }

  function base64UrlEncode($data)
  {
    return \str_replace('=', '', \strtr(\base64_encode($data), '+/', '-_'));
  }
};
