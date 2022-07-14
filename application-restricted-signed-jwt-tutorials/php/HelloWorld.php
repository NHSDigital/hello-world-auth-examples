<?php
require (__DIR__.'php\auth\JwtHandler.php');
use php\auth\JwtHandler as jwthandler;


$tokenUrl = getenv("TOKEN_URL");
$privateKeyFile = getenv("KEY_FILE");
$clientId = getenv("CLIENT_ID");
$kid = getenv("KID");
$jwth = new jwthandler($tokenUrl, $privateKeyFile, $clientId, $kid);


?>