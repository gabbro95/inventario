package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ChiaveValoreDAO;
import com.gabbro95.inventario.model.Utente; // Per controllo sessione

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 	
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ChiaveValoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ChiaveValoreDAO chiaveValoreDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        chiaveValoreDAO = new ChiaveValoreDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controllo sessione utente
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            // Se l'utente non è loggato, reindirizza alla pagina di login
            // Assicurati che "index.jsp" o un'altra pagina di login sia appropriata
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Recupero parametri dal form
        String oggettoIdStr = request.getParameter("oggettoId");
        String chiave = request.getParameter("chiave");
        String valore = request.getParameter("valore");
        
        // Recuperiamo contenitoreId per il redirect. 
        // Questo parametro DEVE essere inviato dal form nella JSP.
        String contenitoreIdStr = request.getParameter("contenitoreId"); 

        // Validazione di base dei parametri
        if (oggettoIdStr == null || oggettoIdStr.trim().isEmpty() ||
            chiave == null || chiave.trim().isEmpty() ||
            valore == null || valore.trim().isEmpty() ||
            contenitoreIdStr == null || contenitoreIdStr.trim().isEmpty()) {
            
            // Parametri mancanti o non validi
            // Potresti voler reindirizzare a una pagina di errore o alla pagina precedente con un messaggio
            request.setAttribute("erroreGenerico", "Tutti i campi (oggettoId, chiave, valore, contenitoreId) sono obbligatori.");
            // Determina a quale pagina reindirizzare o fare forward con l'errore
            // Ad esempio, se venivi da oggetti.jsp:
            if (contenitoreIdStr != null && !contenitoreIdStr.trim().isEmpty()){
                 response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreIdStr + "&erroreCV=campi_mancanti");
            } else if (oggettoIdStr != null && !oggettoIdStr.trim().isEmpty()) {
                 // Se non hai contenitoreId, potresti dover fare una query per ottenerlo dall'oggettoId
                 // o reindirizzare a una pagina di errore generica.
                 // Per ora, un fallback se contenitoreId non c'è ma oggettoId sì:
                 response.sendRedirect(request.getContextPath() + "/dashboard?erroreCV=campi_mancanti_o_id_contenitore_non_valido");
            }
            else {
                response.sendRedirect(request.getContextPath() + "/dashboard?erroreCV=errore_generico_cv");
            }
            return;
        }

        try {
            int oggettoId = Integer.parseInt(oggettoIdStr);
            int contenitoreId = Integer.parseInt(contenitoreIdStr); // Necessario per il redirect

            // Chiamata al DAO per aggiungere la coppia chiave-valore
            chiaveValoreDAO.aggiungiChiaveValore(oggettoId, chiave, valore);

            // Redirect alla pagina degli oggetti del contenitore specifico
            // per vedere l'oggetto aggiornato con la nuova chiave-valore.
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId);

        } catch (NumberFormatException e) {
            // Errore nella conversione degli ID numerici
            // Log dell'errore
            System.err.println("Errore di formato numero in ChiaveValoreServlet: " + e.getMessage()); 
            // Gestione dell'errore: reindirizza con un messaggio di errore
            // È importante avere contenitoreId per un redirect sensato, altrimenti dashboard
             if (contenitoreIdStr != null && !contenitoreIdStr.trim().isEmpty()){
                 response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreIdStr + "&erroreCV=id_non_valido");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard?erroreCV=id_oggetto_o_contenitore_non_valido");
            }
        } catch (Exception e) {
            // Altri errori durante l'operazione DAO
            System.err.println("Errore generico in ChiaveValoreServlet: " + e.getMessage());
            e.printStackTrace(); // Utile per il debug, considera un logger in produzione
            // Reindirizza a una pagina di errore generica o alla dashboard
            response.sendRedirect(request.getContextPath() + "/dashboard?erroreCV=salvataggio_fallito");
        }
    }
}
