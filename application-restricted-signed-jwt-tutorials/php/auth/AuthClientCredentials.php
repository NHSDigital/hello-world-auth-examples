<?php
namespace php\auth;
require_once "JwtHandler.php";

require_once "JwtHandler.php";

class AuthClientCredentials extends JwtHandler
{
    // public $audience;
    // public $client_id;
    // public $kid;
    // public $key;
    public $tokenUrl;
    public $jwt;
    //public  $JwtHandler $jwtHandler;

    //function __construct($tokenUrl, $privateKeyFile, $clientId, $kid  )
    function __construct($tokenUrl, $jwt)
        {
        $this->tokenUrl = $tokenUrl;
        $this->jwt = $jwt;
        //$jwtHandler = new JwtHandler($privateKeyFile, $this->tokenUrl, $this->clientId, $kid);

        
      }


    

    // public AuthClientCredentials(string tokenUrl, string privateKeyFile, string clientId, string kid, HttpClient? client = null)
    // {
    //     _tokenUrl = tokenUrl;
    //     _client = client ?? new HttpClient();
    //     _jwtHandler = new JwtHandler(privateKeyFile, tokenUrl, clientId, kid);
    // }

    public function AccessToken()
    {
        $url = $this->tokenUrl;
       // $jwtHandler = new JwtHandler($privateKeyFile, $this->tokenUrl, $this->clientId, $kid);
       // $jwth = $this->jwtHandler;
         $jwth = $this->GenerateJwt();
         echo $jwth. "\r\n";
         //$jwt = $this=>jwt;
         echo $this->jwt."\r\n";

        $headers = array(
            "Content-Type: application/x-www-form-urlencoded"
                );
        $data = array("grant_type"=>"client_credentials",
                       "client_assertion_type"=>"urn:ietf:params:oauth:client-assertion-type:jwt-bearer",
                       "client_assertion"=>$this->jwt
                    ) ;    
        print_r($data);  
        //var_dump($data);           

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
        var_dump($resp);
       $json = json_decode($resp); 
       //echo $json;
       $accessToken = $json->access_token;
       echo '\r\n' .$accessToken."\r\n" ; 
       return $accessToken;    

    }

}   
?>