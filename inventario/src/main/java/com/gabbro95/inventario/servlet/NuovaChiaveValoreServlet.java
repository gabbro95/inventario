package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ChiaveValoreDAO;
import com.gabbro95.inventario.dao.OggettoDAO;
import com.gabbro95.inventario.model.Oggetto;
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/nuova-chiave-valore")
public class NuovaChiaveValoreServlet extends HttpServlet {

    private OggettoDAO oggettoDAO;
    private ChiaveValoreDAO chiaveValoreDAO;

    @Override
    public void init() {
        oggettoDAO = new OggettoDAO();
        chiaveValoreDAO = new ChiaveValoreDAO();
    }

    /**
     * Mostra il form per aggiungere un nuovo dettaglio (chiave-valore).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            int oggettoId = Integer.parseInt(request.getParameter("oggettoId"));
            Oggetto oggetto = oggettoDAO.getOggettoById(oggettoId); // Usa il nuovo metodo del DAO

            if (oggetto != null) {
                request.setAttribute("oggetto", oggetto);
                request.setAttribute("contenitoreId", request.getParameter("contenitoreId")); // Passiamo anche il contenitoreId
                RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/nuova_chiave_valore.jsp");
                dispatcher.forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard?errore=Oggetto+non+trovato");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Richiesta+non+valida");
        }
    }

    /**
     * Salva il nuovo dettaglio (chiave-valore) nel database.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        try {
            String chiave = request.getParameter("chiave");
            String valore = request.getParameter("valore");
            int oggettoId = Integer.parseInt(request.getParameter("oggettoId"));
            String contenitoreId = request.getParameter("contenitoreId"); // Lo recuperiamo per il redirect

            // Validazione
            if (chiave == null || chiave.trim().isEmpty()) {
                 response.sendRedirect(request.getContextPath() + "/nuova-chiave-valore?oggettoId=" + oggettoId + "&contenitoreId=" + contenitoreId + "&errore=La+chiave+non+puo+essere+vuota");
                 return;
            }

            chiaveValoreDAO.aggiungiChiaveValore(oggettoId, chiave, valore);

            // Reindirizza alla lista oggetti
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Dettaglio+aggiunto");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=Errore+interno");
        }
    }
}