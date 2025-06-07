// File: src/main/java/com/gabbro95/inventario/servlet/GestisciChiaveValoreServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ChiaveValoreDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GestisciChiaveValoreServlet extends HttpServlet {
    
    private ChiaveValoreDAO chiaveValoreDAO;

    @Override
    public void init() {
        chiaveValoreDAO = new ChiaveValoreDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Controlliamo che l'utente sia loggato (logica da aggiungere se necessario)
        
        String action = request.getParameter("action");
        String contenitoreId = request.getParameter("contenitoreId"); // Per il redirect

        if ("delete".equals(action)) {
            try {
                int kvId = Integer.parseInt(request.getParameter("kvId"));
                chiaveValoreDAO.deleteChiaveValore(kvId);
                response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Dettaglio+eliminato");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&errore=Eliminazione+fallita");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&errore=Azione+non+valida");
        }
    }
}