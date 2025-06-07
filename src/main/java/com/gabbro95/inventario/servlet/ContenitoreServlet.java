package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;

public class ContenitoreServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private ContenitoreDAO contenitoreDAO;

    @Override
    public void init() {
        contenitoreDAO = new ContenitoreDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?errore=sessione_scaduta");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");
        int utenteId = utente.getId();

        String nomeContenitore = request.getParameter("nome");

        if (nomeContenitore == null || nomeContenitore.trim().isEmpty()) {
            System.out.println("ERRORE: Il nome del contenitore è vuoto."); // DEBUG
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Il+nome+del+contenitore+non+puo+essere+vuoto");
            return;
        }

        try {
            contenitoreDAO.creaContenitore(nomeContenitore.trim(), utenteId);

            response.sendRedirect(request.getContextPath() + "/dashboard?successo=Contenitore+creato");

        } catch (Exception e) {
            e.printStackTrace(); 
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Si+e+verificato+un+errore+nel+database");
        }
    }
}
