<?php
namespace App\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;


class HelloAuthController extends AbstractController
{
    

    #[Route('/')]
    public function index(): Response
    {
        return $this->render('hellotut/index.html.twig');
    }


    #[Route('/auth')]
    public function auth()
    {
        $options = [
            'scope' => ['nhs-login']
        ];

        $provider = new \League\OAuth2\Client\Provider\GenericProvider([
            'clientId'                => $_ENV["CLIENT_ID"] ,    // The client ID assigned to you by the provider
            'clientSecret'            => $_ENV["CLIENT_SECRET"],    // The client password assigned to you by the provider
            'redirectUri'             => 'http://localhost:8000/hello',
            'urlAuthorize'            => 'https://sandbox.api.service.nhs.uk/oauth2/authorize',
            'urlAccessToken'          => 'https://sandbox.api.service.nhs.uk/oauth2/token',
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
    public function hello_landing(): Response
    {
        $provider = new \League\OAuth2\Client\Provider\GenericProvider([
            'clientId'                => $_ENV["CLIENT_ID"] ,    // The client ID assigned to you by the provider
            'clientSecret'            => $_ENV["CLIENT_SECRET"],    // The client password assigned to you by the provider
            'redirectUri'             => 'http://localhost:8000/hello',
            'urlAuthorize'            => 'https://sandbox.api.service.nhs.uk/oauth2/authorize',
            'urlAccessToken'          => 'https://sandbox.api.service.nhs.uk/oauth2/token',
            'urlResourceOwnerDetails' => ''
        ]);

        try {
            
            // Try to get an access token using the authorization code grant.
            $accessToken = $provider->getAccessToken('authorization_code', [
                'code' => $_GET['code']
            ]);
            
            // We have an access token, which we may use in authenticated
            // requests against the service provider's API.
            $access_token = $accessToken->getToken();

    
            $options['headers']['Content-Type'] = 'application/json';
            $options['headers']['Accept'] = 'application/json';
            $request = $provider->getAuthenticatedRequest( 'GET', 'https://sandbox.api.service.nhs.uk/hello-world/hello/user', $accessToken, $options );
            $response = $provider->getParsedResponse( $request );
            $html_response = implode('' ,$response);

        } catch (\League\OAuth2\Client\Provider\Exception\IdentityProviderException $e) {

            // Failed to get the access token or user details.
            exit($e->getMessage());

        }
        
        return $this->render('hellotut/auth.html.twig', [
            'number' => $html_response
        ]);
    }

   
}