<?php
//require_once __DIR__ .'/auth/JwtHandler.php';
require_once 'auth/JwtHandler.php';
require_once 'auth/AuthClientCredentials.php';
use php\auth\JwtHandler;

use php\auth ;

use php\auth\AuthClientCredentials;

$tokenUrl = getenv("TOKEN_URL");
$audience = getenv("TOKEN_URL");
$privateKeyFile = getenv("KEY_FILE");
$clientId = getenv("CLIENT_ID");
$kid = getenv("KID");
// echo $tokenUrl.'\n';
// echo $kid.'\n';
// echo $audience.'\n';
// $auth = new AuthClientCredentials($tokenUrl, $privateKeyFile, $clientId, $kid);
// $accessToken = $auth->AccessToken();
$jwth = new JwtHandler( $privateKeyFile, $audience, $clientId, $kid);
$v = $jwth->GenerateJwt();
//echo $v;
$auth_cred = new AuthClientCredentials($tokenUrl, $v);
$access_token = $auth_cred->AccessToken();

$url = 'https://internal-dev.api.service.nhs.uk/hello-world/hello/application';

$curl = curl_init($url);
curl_setopt($curl, CURLOPT_URL, $url);
curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);

$headers = array(
   "Accept: application/json",
   "Authorization: Bearer {$accessToken}",
);
curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
//for debug only!
curl_setopt($curl, CURLOPT_SSL_VERIFYHOST, false);
curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

$resp = curl_exec($curl);
curl_close($curl);
//var_dump($resp);
echo $resp;



?>

 
?>