package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;
import com.gabbro95.inventario.utils.EmailUtils; // <-- IMPORTANTE
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

        // ... (tutto il codice per ottenere il token e le info utente rimane invariato)
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

        HttpURLConnection userInfoConn = (HttpURLConnection) new URL(USERINFO_URL + "?access_token=" + accessToken).openConnection();
        userInfoConn.setRequestMethod("GET");

        JsonObject userInfo;
        try (InputStreamReader reader = new InputStreamReader(userInfoConn.getInputStream(), StandardCharsets.UTF_8)) {
            userInfo = JsonParser.parseReader(reader).getAsJsonObject();
        }
        
        // --- INIZIO BLOCCO MODIFICATO ---

        // 1. Estrai l'email raw da Google
        String emailDaGoogle = userInfo.get("email").getAsString();

        // 2. NORMALIZZA L'EMAIL!
        String emailNormalizzata = EmailUtils.normalizeGmail(emailDaGoogle);

        // 3. Cerca se un utente con questa email normalizzata esiste già
        Utente utente = utenteDAO.trovaPerEmail(emailNormalizzata);

        String nome = userInfo.has("name") ? userInfo.get("name").getAsString() : "";
        String immagine = userInfo.has("picture") ? userInfo.get("picture").getAsString() : "";

        if (utente != null) {
            // L'UTENTE ESISTE GIÀ: Aggiorna i suoi dati se necessario
            utente.setNome(nome);
            utente.setImmagineProfilo(immagine);
        } else {
            // L'UTENTE NON ESISTE: Creane uno nuovo usando l'email normalizzata
            utente = new Utente(emailNormalizzata, nome, immagine, LocalDate.now());
        }

        // 4. Salva o aggiorna l'utente nel database
        utenteDAO.salvaOUAggiorna(utente);

        // È una buona pratica ricaricare l'utente per avere l'oggetto completo dal DB (con l'ID corretto)
        Utente utentePerSessione = utenteDAO.trovaPerEmail(emailNormalizzata);

        // 5. Imposta l'utente in sessione e reindirizza
        HttpSession session = request.getSession();
        session.setAttribute("utente", utentePerSessione);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}