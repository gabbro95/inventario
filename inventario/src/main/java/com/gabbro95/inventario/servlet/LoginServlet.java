package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        Utente utente = utenteDAO.trovaPerEmail(email);
        if (utente == null) {
            utente = new Utente(email, null, null, LocalDate.now()); // nome e immagine nulli
            utenteDAO.inserisciOaggiornaUtente(utente);
        }

        HttpSession session = request.getSession();
        session.setAttribute("utente", utente);

        response.sendRedirect("jsp/dashboard.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.isEmpty()) {
            request.setAttribute("errore", "Email obbligatoria.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        Utente utente = utenteDAO.trovaPerEmail(email);
        if (utente == null) {
            utente = new Utente(email, null, null, LocalDate.now());
            utenteDAO.inserisciOaggiornaUtente(utente);
        }

        HttpSession session = request.getSession();
        session.setAttribute("utente", utente);

        response.sendRedirect("jsp/dashboard.jsp");
    }
}
