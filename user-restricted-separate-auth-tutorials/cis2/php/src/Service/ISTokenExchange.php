<?php

namespace App\Service;

class ISTokenExchange {

    private $subject_token;
    private $client_assertion;
    
  function __construct($subject_token, $client_assertion)
  {
    $this->subject_token = $subject_token;
    $this->client_assertion = $client_assertion;
  }

  public function tokenExchange()
  {
    $headers = array(
        "Content-Type: application/x-www-form-urlencoded"
    );

    $data = array(
        "subject_token" => $this->subject_token,
        'client_secret'           => 'vb81YoTQVqcHrmes',
        "client_assertion" => $this->client_assertion,
        "subject_token_type" => 'urn:ietf:params:oauth:token-type:id_token',
        "client_assertion_type" => 'urn:ietf:params:oauth:client-assertion-type:jwt-bearer',
        "grant_type" => 'urn:ietf:params:oauth:grant-type:token-exchange'
    );

    $curl = curl_init('https://sandbox.api.service.nhs.uk/oauth2-mock/token');
    curl_setopt($curl, CURLOPT_URL, 'https://sandbox.api.service.nhs.uk/oauth2-mock/token');
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_POSTFIELDS, http_build_query($data));
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);

    $resp = curl_exec($curl);
  
    curl_close($curl);
    $json = json_decode($resp);
    error_log("TOKEN EXCHANGE");
    error_log(json_encode($json, JSON_PRETTY_PRINT));

    return $json->access_token;
  }
};
