package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;

@WebServlet("/oauth2callback")
public class OAuth2CallbackServlet extends HttpServlet {
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    private final UtenteDAO utenteDAO = new UtenteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Codice OAuth mancante.");
            return;
        }

        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
        String redirectUri = request.getRequestURL().toString();

        String tokenData = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                URLEncoder.encode(code, StandardCharsets.UTF_8),
                URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                URLEncoder.encode(clientSecret, StandardCharsets.UTF_8),
                URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
        );

        // Scambia codice per access token
        HttpURLConnection tokenConn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
        tokenConn.setRequestMethod("POST");
        tokenConn.setDoOutput(true);
        tokenConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        tokenConn.getOutputStream().write(tokenData.getBytes(StandardCharsets.UTF_8));

        JsonObject tokenJson;
        try (InputStreamReader reader = new InputStreamReader(tokenConn.getInputStream(), StandardCharsets.UTF_8)) {
            tokenJson = JsonParser.parseReader(reader).getAsJsonObject();
        }

        String accessToken = tokenJson.get("access_token").getAsString();

        // Ottieni info utente
        HttpURLConnection userInfoConn = (HttpURLConnection) new URL(USERINFO_URL + "?access_token=" + accessToken).openConnection();
        userInfoConn.setRequestMethod("GET");

        JsonObject userInfo;
        try (InputStreamReader reader = new InputStreamReader(userInfoConn.getInputStream(), StandardCharsets.UTF_8)) {
            userInfo = JsonParser.parseReader(reader).getAsJsonObject();
        }

        String email = userInfo.get("email").getAsString();
        String nome = userInfo.has("name") ? userInfo.get("name").getAsString() : "";
        String immagine = userInfo.has("picture") ? userInfo.get("picture").getAsString() : "";

        Utente utente = new Utente(email, nome, immagine, LocalDate.now());
        utenteDAO.salvaOUAggiorna(utente);

        HttpSession session = request.getSession();
        session.setAttribute("utente", utente);

        response.sendRedirect("dashboard");
    }
}
