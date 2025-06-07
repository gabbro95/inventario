// File: src/main/java/com/gabbro95/inventario/servlet/NuovoOggettoServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.dao.OggettoDAO;
import com.gabbro95.inventario.model.Contenitore;
import com.gabbro95.inventario.model.Oggetto;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class NuovoOggettoServlet extends HttpServlet {

    private ContenitoreDAO contenitoreDAO;
    private OggettoDAO oggettoDAO;

    @Override
    public void init() {
        contenitoreDAO = new ContenitoreDAO();
        oggettoDAO = new OggettoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?errore=sessione_scaduta");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");
        
        String contenitoreIdStr = request.getParameter("contenitoreId");

        try {
            int contenitoreId = Integer.parseInt(contenitoreIdStr);
            Contenitore contenitore = contenitoreDAO.getContenitoreById(contenitoreId, utente.getId());

            if (contenitore != null) {
                request.setAttribute("contenitore", contenitore);
                
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/nuovo_oggetto.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard?errore=Contenitore+non+trovato");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=ID+contenitore+non+valido");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?errore=sessione_scaduta");
            return;
        }

        try {
            // Leggiamo tutti i parametri dal form
            String nomeOggetto = request.getParameter("nomeOggetto");
            int quantita = Integer.parseInt(request.getParameter("quantita"));
            int soglia = Integer.parseInt(request.getParameter("sogliaMinima"));
            int contenitoreId = Integer.parseInt(request.getParameter("contenitoreId"));

            // Validazione
            if (nomeOggetto == null || nomeOggetto.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/nuovo-oggetto?contenitoreId=" + contenitoreId + "&errore=Il+nome+non+puo+essere+vuoto");
                return;
            }
            
            // --- BLOCCO MODIFICATO ---
            // Creiamo l'oggetto e impostiamo TUTTI i suoi campi, inclusa la soglia
            Oggetto nuovoOggetto = new Oggetto();
            nuovoOggetto.setNome(nomeOggetto.trim());
            nuovoOggetto.setNumero(quantita);
            nuovoOggetto.setContenitoreId(contenitoreId);
            nuovoOggetto.setSogliaMinima(soglia); // Impostiamo la soglia letta dal form

            // Passiamo l'oggetto completo al DAO
            oggettoDAO.creaOggetto(nuovoOggetto);
            // --- FINE BLOCCO MODIFICATO ---
            
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Oggetto+creato");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Dati+non+validi");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Errore+interno+del+server");
        }
    }
}