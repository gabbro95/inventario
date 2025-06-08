package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;
import com.gabbro95.inventario.utils.EmailUtils;
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

        String clientId = System.getenv("GOOGLE_CLIENT_ID");
        String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
        // Questo redirectUri DEVE essere identico a quello inviato nella richiesta iniziale
        // e configurato su Google Cloud Console.
        // L'abbiamo risolto con la OAuth2LoginRedirectServlet, riutilizziamo la sua logica.
        StringBuilder redirectUriBuilder = new StringBuilder();
        redirectUriBuilder.append("https").append("://").append(request.getServerName());
        int port = request.getServerPort();
        if (port != 80 && port != 443) {
            redirectUriBuilder.append(":").append(port);
        }
        redirectUriBuilder.append(request.getContextPath()).append("/oauth2callback");
        String redirectUri = redirectUriBuilder.toString();

        // *** AGGIUNGI QUESTI LOG PER IL DEBUG DELLA RICHIESTA TOKEN ***
        System.out.println("DEBUG CALLBACK: Code received: " + code);
        System.out.println("DEBUG CALLBACK: Client ID: " + (clientId != null ? clientId.substring(0, Math.min(clientId.length(), 10)) + "..." : "NULL")); // Log parziale per sicurezza
        System.out.println("DEBUG CALLBACK: Client Secret: " + (clientSecret != null ? clientSecret.substring(0, Math.min(clientSecret.length(), 5)) + "..." : "NULL")); // Log parziale
        System.out.println("DEBUG CALLBACK: Redirect URI for token exchange: " + redirectUri);
        // *** FINE LOG DI DEBUG ***

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

        // Questo è il punto che sta fallendo (IOException: Server returned HTTP response code: 400)
        try {
            tokenConn.getOutputStream().write(tokenData.getBytes(StandardCharsets.UTF_8));

            JsonObject tokenJson;
            // La riga 55 si riferisce a questa parte:
            try (InputStreamReader reader = new InputStreamReader(tokenConn.getInputStream(), StandardCharsets.UTF_8)) {
                tokenJson = JsonParser.parseReader(reader).getAsJsonObject();
            }
            // ... (il resto del tuo codice rimane invariato)
            String accessToken = tokenJson.get("access_token").getAsString();

            HttpURLConnection userInfoConn = (HttpURLConnection) new URL(USERINFO_URL + "?access_token=" + accessToken).openConnection();
            userInfoConn.setRequestMethod("GET");

            JsonObject userInfo;
            try (InputStreamReader reader = new InputStreamReader(userInfoConn.getInputStream(), StandardCharsets.UTF_8)) {
                userInfo = JsonParser.parseReader(reader).getAsJsonObject();
            }

            String emailDaGoogle = userInfo.get("email").getAsString();
            String emailNormalizzata = EmailUtils.normalizeGmail(emailDaGoogle);

            Utente utente = utenteDAO.trovaPerEmail(emailNormalizzata);

            String nome = userInfo.has("name") ? userInfo.get("name").getAsString() : "";
            String immagine = userInfo.has("picture") ? userInfo.get("picture").getAsString() : "";

            if (utente != null) {
                utente.setNome(nome);
                utente.setImmagineProfilo(immagine);
            } else {
                utente = new Utente(emailNormalizzata, nome, immagine, LocalDate.now());
            }

            utenteDAO.salvaOUAggiorna(utente);
            Utente utentePerSessione = utenteDAO.trovaPerEmail(emailNormalizzata);

            HttpSession session = request.getSession();
            session.setAttribute("utente", utentePerSessione);

            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (IOException e) {
            // Questo catch cattura l'IOException che si manifesta
            System.err.println("ERRORE OAUTH TOKEN EXCHANGE: " + e.getMessage());
            e.printStackTrace(); // Stampa lo stack trace completo
            // Tentativo di leggere l'errore stream di Google se disponibile
            try (InputStreamReader errorReader = new InputStreamReader(tokenConn.getErrorStream(), StandardCharsets.UTF_8)) {
                String errorResponse = JsonParser.parseReader(errorReader).getAsJsonObject().toString();
                System.err.println("GOOGLE API ERROR RESPONSE: " + errorResponse);
                response.sendRedirect(request.getContextPath() + "/index.jsp?erroreAuth=Errore+scambio+token+Google:+"+ URLEncoder.encode(errorResponse, StandardCharsets.UTF_8));
            } catch (Exception ex) {
                System.err.println("Errore nel leggere l'ErrorStream di Google: " + ex.getMessage());
                response.sendRedirect(request.getContextPath() + "/index.jsp?erroreAuth=Errore+di+autenticazione+Google.+Dettagli+non+disponibili.");
            }
        } catch (Exception e) { // Cattura altre eccezioni non-IO
            System.err.println("ERRORE GENERICO IN OAUTH CALLBACK: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/index.jsp?erroreAuth=Errore+interno+durante+l'autenticazione.");
        }
    }
}