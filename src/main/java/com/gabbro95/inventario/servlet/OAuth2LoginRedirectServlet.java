// File: src/main/java/com/gabbro95/inventario/servlet/OAuth2LoginRedirectServlet.java
package com.gabbro95.inventario.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class OAuth2LoginRedirectServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        
        String redirectUri = request.getScheme() + "://" +
                             request.getServerName();
        
        int serverPort = request.getServerPort();
        if (serverPort != 80 && serverPort != 443) {
            redirectUri += ":" + serverPort;
        }

        redirectUri += request.getContextPath() + "/oauth2callback";

        String scope = "openid email profile";
        String responseType = "code";

        String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_type=" + URLEncoder.encode(responseType, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

        response.sendRedirect(authUrl);
    }
}