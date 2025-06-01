package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.UtenteDAO;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

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

        String email = request.getParameter("email"); // da OAuth

        if (email == null || email.isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        Utente utente = utenteDAO.trovaPerEmail(email);
        if (utente == null) {
            utente = new Utente(email);
            utenteDAO.creaUtente(utente);
        }

        HttpSession session = request.getSession();
        session.setAttribute("utente", utente);

        response.sendRedirect("dashboard.jsp");
    }
}
