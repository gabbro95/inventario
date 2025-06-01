package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.model.Utente;
import com.gabbro95.inventario.dao.UtenteDAO;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String CLIENT_ID = "TUO_CLIENT_ID";
    private static final String CLIENT_SECRET = "TUO_CLIENT_SECRET";
    private static final String REDIRECT_URI = "http://localhost:8080/inventario/oauth2callback";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String code = req.getParameter("code");

            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    REDIRECT_URI
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            String email = idToken.getPayload().getEmail();

            // 🔁 Reindirizza alla LoginServlet con l’email
            resp.sendRedirect("login?email=" + email);

        } catch (GeneralSecurityException | IOException e) {
            throw new ServletException(e);
        }
    }
}
