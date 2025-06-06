// File: src/main/java/com/gabbro95/inventario/servlet/ProfiloServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/profilo")
public class ProfiloServlet extends HttpServlet {
    private UtenteDAO utenteDAO;

    @Override
    public void init() {
        utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Il doGet mostra semplicemente la pagina, i dati sono già nella sessione
        request.getRequestDispatcher("/jsp/profilo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        Utente utenteCorrente = (Utente) session.getAttribute("utente");
        
        try {
            // Leggi i nuovi dati dal form
            String nuovoNome = request.getParameter("nome");
            String nuovaImmagine = request.getParameter("immagineProfilo");

            // Aggiorna l'oggetto utente
            utenteCorrente.setNome(nuovoNome);
            utenteCorrente.setImmagineProfilo(nuovaImmagine);
            
            // Salva le modifiche nel database
            utenteDAO.updateUtente(utenteCorrente);

            // FONDAMENTALE: Aggiorna l'oggetto anche nella sessione!
            // Altrimenti vedrai i vecchi dati nella navbar fino al prossimo login.
            session.setAttribute("utente", utenteCorrente);

            response.sendRedirect(request.getContextPath() + "/profilo?successo=true");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/profilo?errore=Salvataggio+fallito");
        }
    }
}