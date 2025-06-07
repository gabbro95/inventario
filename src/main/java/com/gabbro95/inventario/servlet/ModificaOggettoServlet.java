package com.gabbro95.inventario.servlet;

import java.io.IOException;

import com.gabbro95.inventario.dao.OggettoDAO;
import com.gabbro95.inventario.model.Oggetto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/modifica-oggetto")
public class ModificaOggettoServlet extends HttpServlet {
    private OggettoDAO oggettoDAO;

    @Override
    public void init() { oggettoDAO = new OggettoDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Logica per MOSTRARE il form pre-compilato
        try {
            int oggettoId = Integer.parseInt(request.getParameter("oggettoId"));
            Oggetto oggetto = oggettoDAO.getOggettoById(oggettoId);
            // Qui andrebbe un controllo per verificare che l'utente sia il proprietario dell'oggetto
            request.setAttribute("oggetto", oggetto);
            request.getRequestDispatcher("/jsp/modifica_oggetto.jsp").forward(request, response);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=oggetto_non_trovato");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Leggiamo TUTTI i dati dal form
            int oggettoId = Integer.parseInt(request.getParameter("oggettoId"));
            int contenitoreId = Integer.parseInt(request.getParameter("contenitoreId")); // Lo usiamo per il redirect
            String nome = request.getParameter("nome");
            int numero = Integer.parseInt(request.getParameter("numero"));
            int sogliaMinima = Integer.parseInt(request.getParameter("sogliaMinima"));
            
            // Creiamo l'oggetto completo con i dati aggiornati
            Oggetto oggettoModificato = new Oggetto();
            oggettoModificato.setId(oggettoId);
            oggettoModificato.setNome(nome);
            oggettoModificato.setNumero(numero);
            oggettoModificato.setSogliaMinima(sogliaMinima);
            
            // Chiamiamo il metodo DAO per l'aggiornamento
            oggettoDAO.updateOggetto(oggettoModificato);
            
            // Reindirizziamo alla lista oggetti per vedere le modifiche
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Oggetto+modificato");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=modifica_fallita");
        }
    }
}