package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.OggettoDAO;
import com.gabbro95.inventario.model.Oggetto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/oggetti")
public class OggettoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OggettoDAO oggettoDAO;

    @Override
    public void init() throws ServletException {
        oggettoDAO = new OggettoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String contenitoreIdParam = request.getParameter("contenitoreId");
        if (contenitoreIdParam != null) {
            try {
                int contenitoreId = Integer.parseInt(contenitoreIdParam);
                List<Oggetto> lista = oggettoDAO.trovaPerContenitoreId(contenitoreId);
                request.setAttribute("listaOggetti", lista);
                request.setAttribute("contenitoreId", contenitoreId);
                request.getRequestDispatcher("/WEB-INF/oggetti.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID contenitore non valido.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID contenitore mancante.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String numeroStr = request.getParameter("numero");
        String contenitoreIdStr = request.getParameter("contenitoreId");

        if (nome != null && numeroStr != null && contenitoreIdStr != null) {
            try {
                int numero = Integer.parseInt(numeroStr);
                int contenitoreId = Integer.parseInt(contenitoreIdStr);
                Oggetto oggetto = new Oggetto(nome, numero, LocalDate.now(), contenitoreId);
                oggettoDAO.creaOggetto(oggetto);
                response.sendRedirect("oggetti?contenitoreId=" + contenitoreId);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Valori numerici non validi.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parametri mancanti.");
        }
    }
}
