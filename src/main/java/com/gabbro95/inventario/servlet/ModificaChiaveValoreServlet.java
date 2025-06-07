// File: src/main/java/com/gabbro95/inventario/servlet/ModificaChiaveValoreServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ChiaveValoreDAO;
import com.gabbro95.inventario.model.ChiaveValore;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ModificaChiaveValoreServlet extends HttpServlet {
    private ChiaveValoreDAO chiaveValoreDAO;

    @Override
    public void init() {
        chiaveValoreDAO = new ChiaveValoreDAO();
    }
    
    // Questo metodo mostra il form con i dati da modificare
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int kvId = Integer.parseInt(request.getParameter("kvId"));
            ChiaveValore kv = chiaveValoreDAO.getChiaveValoreById(kvId);
            
            // Aggiungo anche il contenitoreId per poter tornare indietro correttamente
            String contenitoreId = request.getParameter("contenitoreId");

            request.setAttribute("dettaglio", kv);
            request.setAttribute("contenitoreId", contenitoreId);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/modifica_dettaglio.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=dettaglio_non_trovato");
        }
    }

    // Questo metodo salva le modifiche inviate dal form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int kvId = Integer.parseInt(request.getParameter("kvId"));
            String chiave = request.getParameter("chiave");
            String valore = request.getParameter("valore");
            String contenitoreId = request.getParameter("contenitoreId"); // Per il redirect

            ChiaveValore kv = new ChiaveValore();
            kv.setId(kvId);
            kv.setChiave(chiave);
            kv.setValore(valore);

            chiaveValoreDAO.updateChiaveValore(kv);
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Dettaglio+modificato");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=modifica_dettaglio_fallita");
        }
    }
}