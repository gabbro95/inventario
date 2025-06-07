package com.gabbro95.inventario.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/oauth2login")
public class OAuth2LoginRedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        
        // Costruzione dinamica del redirect URI
        String redirectUri = request.getScheme() + "://" +
                             request.getServerName() + ":" +
                             request.getServerPort() +
                             request.getContextPath() + "/oauth2callback";

        String scope = "openid email profile";
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
