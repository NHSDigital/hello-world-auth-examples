<?php

$clientId = getenv("CLIENT_ID");
$endpoint = getenv("ENDPOINT");

$response = sendRequest($clientId, $endpoint);
echo "Response from Hello World API: " . $response . "\r\n";

function sendRequest($clientId, $endpoint)
{
  $curl = curl_init($endpoint);
  curl_setopt($curl, CURLOPT_URL, $endpoint);
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

  $headers = array(
      "Accept: application/json",
      "apikey: $clientId",
  );
  curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
  $resp = curl_exec($curl);
  curl_close($curl);
  return $resp;
}
