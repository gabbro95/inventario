package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.model.Contenitore;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
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
	// --- INIZIO BLOCCO DI DEBUG TEMPORANEO ---
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Test App Heroku</title></head>");
        out.println("<body>");
        out.println("<h1>Funziona! La mia app risponde da Heroku!</h1>");
        out.println("<p>Se vedi questa pagina, significa che DashboardServlet è stato raggiunto con successo.</p>");
        out.println("<p>URL richiesto: " + request.getRequestURL() + "</p>");
        out.println("<p>Context Path: " + request.getContextPath() + "</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
        // --- FINE BLOCCO DI DEBUG TEMPORANEO ---
/*
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
*/
    }
}
