<?php
namespace php\auth;
require_once "JwtHandler.php";
use php\auth\JwtHandler;

class AuthClientCredentials 
{
    public $tokenUrl;
    public $privateKeyFile;
    public $clientId;
    public $kid;
    
    function __construct($tokenUrl, $privateKeyFile, $clientId, $kid)
    {
        $this->tokenUrl = $tokenUrl;
        $this->privateKeyFile =$privateKeyFile;
        $this->clientId = $clientId;
        $this->kid = $kid;
    }

    public function AccessToken()
    {
        $url = $this->tokenUrl;
        $jwtHandler = new JwtHandler($this->privateKeyFile, $this->tokenUrl, $this->clientId, $this->kid);
        $jwt = $jwtHandler->GenerateJwt();
        
        $headers = array(
            "Content-Type: application/x-www-form-urlencoded"
                );
        $data = array("grant_type"=>"client_credentials",
                       "client_assertion_type"=>"urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                       "client_assertion"=>$jwt
                    ) ;    
        
        $curl = curl_init($url);
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));                                    
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);               
        
        curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
        //for debug only!
        curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

        $resp = curl_exec($curl);
        curl_close($curl);
        $json = json_decode($resp); 
        $accessToken = $json->access_token;
        return $accessToken;    
    }
}   
?>