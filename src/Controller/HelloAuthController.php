<?php
// src/Controller/LuckyController.php
namespace App\Controller;

use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;

class HelloAuthController extends AbstractController
{
    #[Route('/')]
    public function index(): Response
    {
        return $this->render('hellotut/index.html.twig', [
            'number' => 'Andre',
        ]);
    }


    #[Route('/auth')]
    public function auth(): Response
    {
        return $this->render('hellotut/auth.html.twig', [
            'number' => "oauth_endpoint"
        ]);
    }
}