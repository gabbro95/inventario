package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Contenitore;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

public class DashboardServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private ContenitoreDAO contenitoreDAO;

    @Override
    public void init() {
        contenitoreDAO = new ContenitoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente utente = (Utente) session.getAttribute("utente");
        ContenitoreDAO contenitoreDAO = new ContenitoreDAO(); // O come lo inizializzi

        // Prendi l'ID dall'oggetto Utente e passalo al metodo aggiornato del DAO
        List<Contenitore> contenitori = contenitoreDAO.getContenitoriPerUtente(utente.getId()); //

        request.setAttribute("contenitori", contenitori);
        request.getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }
}
