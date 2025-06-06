// File: src/main/java/com/gabbro95/inventario/servlet/CarrelloServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.CarrelloDAO;
import com.gabbro95.inventario.model.CarrelloItem;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
    private CarrelloDAO carrelloDAO;
    private static final int SOGLIA_CARRELLO = 5; // Imposta qui la soglia che preferisci

    @Override
    public void init() {
        carrelloDAO = new CarrelloDAO();
    }

    /**
     * Mostra la pagina del carrello.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");

        // 1. Popola il carrello con gli oggetti sotto soglia
        carrelloDAO.popolaCarrelloAutomaticamente(utente.getId());
        
        // 2. Recupera gli elementi aggiornati del carrello
        List<CarrelloItem> carrelloItems = carrelloDAO.getCarrelloPerUtente(utente.getId());
        
        // 3. Passa la lista alla JSP
        request.setAttribute("carrelloItems", carrelloItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/carrello.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce l'aggiornamento degli oggetti dopo la spesa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
                
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");

        // Recupera solo gli ID degli oggetti selezionati (checkbox)
        String[] oggettiSelezionatiIds = request.getParameterValues("oggettoId");

        if (oggettiSelezionatiIds == null || oggettiSelezionatiIds.length == 0) {
            response.sendRedirect(request.getContextPath() + "/carrello?errore=Nessun+oggetto+selezionato");
            return;
        }

        // Usiamo una Mappa per associare ogni ID alla sua quantità
        java.util.Map<Integer, Integer> oggettiDaAggiornare = new java.util.HashMap<>();
        
        try {
            // Cicliamo su ogni ID selezionato
            for (String idStr : oggettiSelezionatiIds) {
                int oggettoId = Integer.parseInt(idStr);
                
                // Costruiamo il nome del campo quantità corrispondente (es. "quantita_12")
                String nomeParametroQuantita = "quantita_" + oggettoId;
                String quantitaStr = request.getParameter(nomeParametroQuantita);
                
                int quantitaDaAggiungere = Integer.parseInt(quantitaStr);
                
                // Aggiungiamo la coppia (ID, Quantità) alla mappa
                oggettiDaAggiornare.put(oggettoId, quantitaDaAggiungere);
            }

            // Chiama il DAO passando la Mappa di oggetti e quantità
            carrelloDAO.aggiornaOggettiESvuotaCarrello(oggettiDaAggiornare, utente.getId());
            response.sendRedirect(request.getContextPath() + "/carrello?successo=Inventario+aggiornato");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carrello?errore=Una+delle+quantita+inserite+non+e+un+numero+valido.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carrello?errore=Errore+interno+durante+l'aggiornamento.");
        }
    }
}