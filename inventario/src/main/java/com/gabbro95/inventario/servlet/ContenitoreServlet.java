package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Contenitore;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/contenitori")
public class ContenitoreServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;


    private ContenitoreDAO contenitoreDAO;

    @Override
    public void init() throws ServletException {
        contenitoreDAO = new ContenitoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");
        List<Contenitore> contenitori = contenitoreDAO.trovaPerEmailUtente(email);

        request.setAttribute("contenitori", contenitori);
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
