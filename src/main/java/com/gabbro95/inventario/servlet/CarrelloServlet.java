package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.CarrelloDAO;
import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.CarrelloItem;
import com.gabbro95.inventario.model.Contenitore;
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
import java.util.stream.Collectors;

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
    private CarrelloDAO carrelloDAO;
    private ContenitoreDAO contenitoreDAO;

    @Override
    public void init() {
        carrelloDAO = new CarrelloDAO();
        contenitoreDAO = new ContenitoreDAO();
    }

    /**
     * Mostra la pagina del carrello con opzioni di filtro per contenitore.
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

        // 1. Popola il carrello con gli oggetti sotto soglia (assicura che sia aggiornato)
        carrelloDAO.popolaCarrelloAutomaticamente(utente.getId());

        // 2. Recupera tutti i contenitori dell'utente per la selectbox del filtro
        // CORREZIONE QUI: Usa il nome del metodo corretto getContenitoriPerUtente
        List<Contenitore> listaContenitori = contenitoreDAO.getContenitoriPerUtente(utente.getId());
        request.setAttribute("listaContenitori", listaContenitori);

        // 3. Recupera gli elementi aggiornati del carrello
        List<CarrelloItem> carrelloItems = carrelloDAO.getCarrelloPerUtente(utente.getId());

        // --- Logica di filtro per contenitore ---
        String filterContenitoreIdStr = request.getParameter("filterContenitoreId");
        if (filterContenitoreIdStr != null && !filterContenitoreIdStr.isEmpty()) {
            try {
                int filterContenitoreId = Integer.parseInt(filterContenitoreIdStr);
                // Manteniamo il parametro selezionato per la JSP
                request.setAttribute("selectedContenitoreId", filterContenitoreId);

                final int fci = filterContenitoreId;
                carrelloItems = carrelloItems.stream()
                                             .filter(item -> item.getContenitoreId() == fci)
                                             .collect(Collectors.toList());

            } catch (NumberFormatException e) {
                // Ignora o gestisci l'errore se il parametro non è un numero valido
                System.err.println("ID Contenitore di filtro non valido: " + filterContenitoreIdStr);
            }
        }

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

        String[] oggettiSelezionatiIds = request.getParameterValues("oggettoId");

        if (oggettiSelezionatiIds == null || oggettiSelezionatiIds.length == 0) {
            // Reindirizza mantenendo i parametri di filtro se presenti
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Nessun+oggetto+selezionato";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        java.util.Map<Integer, Integer> oggettiDaAggiornare = new java.util.HashMap<>();

        try {
            for (String idStr : oggettiSelezionatiIds) {
                int oggettoId = Integer.parseInt(idStr);
                String nomeParametroQuantita = "quantita_" + oggettoId;
                String quantitaStr = request.getParameter(nomeParametroQuantita);

                int quantitaDaAggiungere = Integer.parseInt(quantitaStr);

                oggettiDaAggiornare.put(oggettoId, quantitaDaAggiungere);
            }

            carrelloDAO.aggiornaOggettiESvuotaCarrello(oggettiDaAggiornare, utente.getId());
            // Reindirizza mantenendo i parametri di filtro se presenti
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?successo=Inventario+aggiornato";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Una+delle+quantita+inserite+non+e+un+numero+valido.";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Errore+interno+durante+l'aggiornamento.";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
        }
    }
}