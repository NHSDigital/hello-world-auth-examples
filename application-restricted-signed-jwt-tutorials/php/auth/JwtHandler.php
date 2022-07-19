<?php
namespace php\auth;
class JwtHandler
{
    public $audience;
    public $client_id;
    public $kid;
    public $key;

    function __construct($key, $audience, $client_id, $kid) 
      {
        $this->audience = $audience;
        $this->client_id = $client_id;
        $this->kid = $kid;
        $this->key = $key;
      }

    public  function GenerateJwt()
    {
      
      $claims = [
        "sub"=> $this->client_id,
        "iss"=> $this->client_id,
        "jti"=> uniqid(),
        "aud"=> $this->audience,
        "exp"=> (time()) + 300 # 5mins in the future
      ];
      $fp = fopen('jwtRS512.key',"r");
      $privateKey = fread($fp,8192);
      $additional_headers = ["alg" => "RS512",
                              "typ" => "JWT",
                              "kid"=> $this->kid];
      $headers_encoded = $this->base64UrlEncode(json_encode($additional_headers));
      $payload_encoded = $this->base64UrlEncode(json_encode($claims));
      
      openssl_sign("$headers_encoded.$payload_encoded", $signature, $privateKey, 'sha512WithRSAEncryption');
      $signature_encoded = $this->base64UrlEncode($signature);

      //build and return the token
      $jwt_token = "$headers_encoded.$payload_encoded.$signature_encoded";
      return $jwt_token;
            
    } 

   function base64UrlEncode(string $data): string
      {
         return \str_replace('=', '', \strtr(\base64_encode($data), '+/', '-_'));
      }  
    
}
?>