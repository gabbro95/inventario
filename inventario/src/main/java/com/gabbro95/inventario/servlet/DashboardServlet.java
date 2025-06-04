package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Contenitore;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private ContenitoreDAO contenitoreDAO;

    @Override
    public void init() {
        contenitoreDAO = new ContenitoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        List<Contenitore> contenitori = contenitoreDAO.getContenitoriPerUtente(utente.getEmail());
        request.setAttribute("contenitori", contenitori);

        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}
