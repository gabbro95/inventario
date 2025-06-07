package com.gabbro95.inventario.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/oauth2login")
public class OAuth2LoginRedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        
        // Costruzione dinamica del redirect URI
        StringBuilder redirectUriBuilder = new StringBuilder();
        
        // --- MODIFICA 1: Forza lo schema HTTPS per Heroku ---
        // Heroku reindirizza sempre il traffico esterno su HTTPS.
        // Usiamo "https" invece di request.getScheme() che potrebbe restituire "http" internamente.
        redirectUriBuilder.append("https").append("://").append(request.getServerName());

        // --- MODIFICA 2: Gestisci la porta per evitare :80 o :443 espliciti ---
        // request.getServerPort() potrebbe restituire 80 internamente anche per richieste HTTPS esterne,
        // ma l'URL finale per Google non deve includere la porta se è standard (80 per HTTP, 443 per HTTPS).
        int port = request.getServerPort();
        // Aggiungi la porta SOLO se non è la porta standard di HTTP (80) o HTTPS (443)
        if (port != 80 && port != 443) {
            redirectUriBuilder.append(":").append(port);
        }
        
        // --- MODIFICA 3: Aggiungi il Context Path (se presente) ---
        // request.getContextPath() restituirà "/inventario" se la tua app è deployata lì,
        // o "" (stringa vuota) se è deployata alla root. Questo codice si adatta.
        redirectUriBuilder.append(request.getContextPath()); 
        
        // Aggiungi il path specifico della servlet di callback
        redirectUriBuilder.append("/oauth2callback"); 

        String redirectUri = redirectUriBuilder.toString();

        // --- MANTIENI QUESTO LOG PER LA VERIFICA FINALE ---
        // Dopo il deploy, controlla i log di Heroku per vedere l'URL esatto generato.
        System.out.println("DEBUG OAUTH: Redirect URI generato: " + redirectUri);

        String scope = "openid email";
        String responseType = "code";

        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=" + responseType +
                "&scope=" + scope +
                "&access_type=offline";

        response.sendRedirect(authUrl);
    }
}