<?php
namespace php\auth;
class JwtHandler
{
    public $audience;
    public $client_id;
    public $kid;
    public $key;

    function __construct($audience, $client_id, $kid, $key) 
        {
        $this->audience = $audience;
        $this->client_id = $client_id;
        $this->kid = $kid;
        $this->key = $key;
      }


    // $header = [
    //     'alg' => 'RSA',
    //     'typ' => 'JWT'
    //  ];
     
}