package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;
import com.gabbro95.inventario.utils.EmailUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        super.init(); // Buona pratica chiamare super.init()
        utenteDAO = new UtenteDAO();
    }

    // Il metodo doGet potrebbe non essere necessario per il login via form,
    // ma lo lascio se hai un flusso che lo utilizza.
    // Considera se vuoi che il login via email funzioni solo con POST.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se si accede a /login con GET, reindirizza alla pagina di login effettiva (index.jsp)
        // o gestisci il login se i parametri sono presenti (come fa il doPost)
        // Per semplicità, ora reindirizzo a index.jsp se non ci sono parametri specifici per il login GET.
        // Se l'utente è già loggato, potrebbe essere reindirizzato alla dashboard.
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("utente") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        // Se la logica GET è per gestire un login OAuth di ritorno o simile, va bene.
        // Altrimenti, per un semplice form, il POST è più comune.
        // Duplico la logica del POST per ora se viene usato con parametri GET.
        handleLoginRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleLoginRequest(request, response);
    }

    private void handleLoginRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null && existingSession.getAttribute("utente") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("erroreLogin", "L'indirizzo email è obbligatorio.");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }
        
        // --- MODIFICA 1: NORMALIZZA L'EMAIL ---
        // Usa la classe di utilità che hai già creato e importato.
        // Da questo momento in poi, usa SEMPRE la variabile "emailNormalizzata".
        String emailNormalizzata = EmailUtils.normalizeGmail(email.trim());

        try {
            // --- MODIFICA 2: CERCA USANDO L'EMAIL NORMALIZZATA ---
            Utente utente = utenteDAO.trovaPerEmail(emailNormalizzata);
            
            if (utente == null) {
                // Utente non trovato, creane uno nuovo
                String nomeDefault;
                int atSymbolIndex = emailNormalizzata.indexOf('@');
                if (atSymbolIndex > 0) {
                    nomeDefault = emailNormalizzata.substring(0, atSymbolIndex);
                } else {
                    nomeDefault = "Utente";
                }
                if(nomeDefault.trim().isEmpty()){
                    nomeDefault = "Utente_" + System.currentTimeMillis() % 10000;
                }

                // --- MODIFICA 3: CREA L'UTENTE CON L'EMAIL NORMALIZZATA ---
                utente = new Utente(emailNormalizzata, nomeDefault, "", LocalDate.now());
                
                utenteDAO.inserisciOaggiornaUtente(utente);
                
                // Anche qui, recupera l'utente usando l'email normalizzata
                utente = utenteDAO.trovaPerEmail(emailNormalizzata);
            }

            // Crea una nuova sessione e imposta l'utente
            HttpSession session = request.getSession(true); 
            session.setAttribute("utente", utente);

            // Reindirizza alla servlet /dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("erroreLogin", "Si è verificato un errore durante il login. Riprova più tardi. Dettaglio: " + e.getMessage());
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}
