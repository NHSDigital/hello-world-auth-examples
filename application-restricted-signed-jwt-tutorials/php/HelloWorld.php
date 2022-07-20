<?php
require_once __DIR__ .'/auth/AuthClientCredentials.php';
use php\auth\AuthClientCredentials;

$tokenUrl = getenv("TOKEN_URL");
$audience = getenv("TOKEN_URL");
$privateKeyFile = getenv("KEY_FILE");
$clientId = getenv("CLIENT_ID");
$kid = getenv("KID");
$endpoint = getenv("ENDPOINT");
        
$auth = new AuthClientCredentials($tokenUrl, $privateKeyFile, $clientId, $kid);
$accessToken = $auth->AccessToken();
echo"Received access token: ".$accessToken."\r\n";

$response = sendRequest($accessToken, $endpoint);
echo"Response from Hello World API: ".$response."\r\n";

function sendRequest($accessToken, $endpoint)
{
    $curl = curl_init($endpoint);
    curl_setopt($curl, CURLOPT_URL, $endpoint);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

    $headers = array(
    "Accept: application/json",
    "Authorization: Bearer {$accessToken}",
    );
    curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
    $resp = curl_exec($curl);
    curl_close($curl);
    return $resp;
}
