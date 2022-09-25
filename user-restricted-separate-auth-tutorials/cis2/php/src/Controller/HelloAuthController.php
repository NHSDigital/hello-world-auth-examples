<?php
namespace App\Controller;

use App\Service\ISTokenExchange;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use App\Service\JwtHandler;
use App\Service\SeparateAuthHttpClient;
use Error;

class HelloAuthController extends AbstractController {
    
    #[Route(path: '/', name: 'homepage')]
    public function index(): Response
    {
        return $this->render('hellotut/index.html.twig');
    }


    #[Route('/auth')]
    public function auth()
    {
        $options = [
            'scope' => ['openid']
        ];

        $provider = new \League\OAuth2\Client\Provider\GenericProvider([
            'clientId'                => 'hello-world-tutorials' ,    // The client ID assigned to you by the provider 
            'client_secret'           => 'dde091bb-97af-483e-8d3e-02d6592cf318',
            'redirectUri'             => 'http://localhost:8000/hello',
            'urlAuthorize'            => 'https://identity.ptl.api.platform.nhs.uk/auth/realms/Cis2-mock-sandbox/protocol/openid-connect/auth',
            'urlAccessToken'          => 'https://identity.ptl.api.platform.nhs.uk/auth/realms/Cis2-mock-sandbox/protocol/openid-connect/token',
            'scopes' => ['openid', 'nationalrbacaccess'], 
            'urlResourceOwnerDetails' => ''
        ]);

        // If we don't have an authorization code then get one
        if (!isset($_GET['code'])) {    
            $authorizationUrl = $provider->getAuthorizationUrl($options);                      
            // Redirect the user to the authorization URL.
            header('Location: ' . $authorizationUrl);
            exit;
        }
    }


    #[Route('/hello')]
    public function callback(): Response {   
        
        $code = $_GET['code'];
        $IStokenUrl = 'https://sandbox.api.service.nhs.uk/oauth2-mock/token';
        $tokenUrl = 'https://identity.ptl.api.platform.nhs.uk/auth/realms/Cis2-mock-sandbox/protocol/openid-connect/token';
        
        $APPPrivateKey_path = $_ENV['CLIENT_ID'];
        $APPClientSecret = $_ENV['CLIENT_SECRET'];

        try {

            $separateAuthClient = new SeparateAuthHttpClient($code, "", $tokenUrl);
            $token = $separateAuthClient->getAccessToken();
            
            if( !isset( $token->id_token) ){
                return $this->redirectToRoute('homepage');
            }
            
            
            $idToken = $token->id_token;
            error_log("Id TOKEN");
            error_log(json_encode($idToken, JSON_PRETTY_PRINT));
    
            
            $ISJwtHandler = new JwtHandler($APPPrivateKey_path, $IStokenUrl, $APPClientSecret, "test-1", 'RS512', 'sha512WithRSAEncryption');
            $ISjwt = $ISJwtHandler->GenerateJwt();
            
            error_log("IS JWT");
            error_log($ISjwt);
            

            $ISTokenExchangeHandler = new ISTokenExchange($idToken, $ISjwt);
            $finalToken = $ISTokenExchangeHandler->tokenExchange();
            
            $provider = new \League\OAuth2\Client\Provider\GenericProvider([
                'urlAuthorize'=> '', 
                'urlAccessToken'=> '', 
                'urlResourceOwnerDetails' => ''
            ]);
            $options['headers']['Content-Type'] = 'application/json';
            $options['headers']['Accept'] = 'application/json';
            $request = $provider->getAuthenticatedRequest( 'GET', 'https://sandbox.api.service.nhs.uk/hello-world/hello/user', $finalToken, $options );
            $response = $provider->getParsedResponse( $request );
            $html_response = implode('' ,$response);

        } catch (\League\OAuth2\Client\Provider\Exception\IdentityProviderException $e) {
            exit($e->getMessage());
        }

        return $this->render('hellotut/auth.html.twig', [
            'number' => $html_response 
        ]);

    }
   
}

