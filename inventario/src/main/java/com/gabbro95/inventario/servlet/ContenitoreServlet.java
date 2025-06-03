package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/contenitore")
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
        Utente utente = (Utente) (session != null ? session.getAttribute("utente") : null);

        if (utente == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String nome = request.getParameter("nome");
        if (nome != null && !nome.trim().isEmpty()) {
            contenitoreDAO.creaContenitore(nome, utente.getEmail());
        }

        response.sendRedirect("jsp/dashboard.jsp");
    }
}
