package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.ChiaveValoreDAO;
import com.gabbro95.inventario.dao.ContenitoreDAO;
import com.gabbro95.inventario.dao.OggettoDAO;
import com.gabbro95.inventario.model.ChiaveValore;
import com.gabbro95.inventario.model.Contenitore;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections; // Import per Collections.sort
import java.util.Comparator;  // Import per Comparator
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@WebServlet("/oggetti")
public class OggettoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OggettoDAO oggettoDAO;
    private ContenitoreDAO contenitoreDAO;
    private ChiaveValoreDAO chiaveValoreDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        oggettoDAO = new OggettoDAO();
        contenitoreDAO = new ContenitoreDAO();
        chiaveValoreDAO = new ChiaveValoreDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        String contenitoreIdParam = request.getParameter("contenitoreId");
        if (contenitoreIdParam != null && !contenitoreIdParam.trim().isEmpty()) {
            try {
                int contenitoreId = Integer.parseInt(contenitoreIdParam);

                Contenitore contenitore = contenitoreDAO.getContenitoreById(contenitoreId, utente.getId());
                if (contenitore == null) {
                    response.sendRedirect(request.getContextPath() + "/dashboard?errore=contenitore_non_trovato");
                    return;
                }

                List<Oggetto> listaOggetti = oggettoDAO.trovaPerContenitoreId(contenitoreId);

                // --- LOGICA DI ORDINAMENTO AGGIUNTA QUI ---
                String sortBy = request.getParameter("sortBy");
                String sortOrder = request.getParameter("sortOrder"); // "asc" o "desc"

                if (listaOggetti != null && !listaOggetti.isEmpty()) {
                    Comparator<Oggetto> comparator = null;

                    if ("nome".equals(sortBy)) {
                        comparator = Comparator.comparing(Oggetto::getNome);
                    } else if ("numero".equals(sortBy)) {
                        comparator = Comparator.comparingInt(Oggetto::getNumero);
                    } else if ("dataInserimento".equals(sortBy)) {
                        comparator = Comparator.comparing(Oggetto::getDataInserimento);
                    }

                    if (comparator != null) {
                        if ("desc".equals(sortOrder)) {
                            comparator = comparator.reversed();
                        }
                        Collections.sort(listaOggetti, comparator);
                    }
                }
                // --- FINE LOGICA DI ORDINAMENTO ---

                Map<Integer, List<ChiaveValore>> mappaChiaviValore = null;
                if (listaOggetti != null && !listaOggetti.isEmpty()) {
                    List<Integer> idsOggetti = listaOggetti.stream()
                                                           .map(Oggetto::getId)
                                                           .collect(Collectors.toList());
                    if (!idsOggetti.isEmpty()) {
                        mappaChiaviValore = chiaveValoreDAO.getChiaviPerOggetti(idsOggetti);
                    }
                }

                request.setAttribute("contenitore", contenitore);
                request.setAttribute("listaOggetti", listaOggetti);
                request.setAttribute("mappaChiaviValore", mappaChiaviValore);

                request.getRequestDispatcher("/jsp/oggetti.jsp").forward(request, response);

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/dashboard?errore=id_contenitore_invalido");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard?errore=id_contenitore_mancante");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

        if (utente == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        // Leggiamo il parametro 'action' per decidere cosa fare
        String action = request.getParameter("action");
        if (action == null) {
            // Se l'azione non è specificata, assumiamo sia una creazione
            // (per compatibilità con il form di nuovo_oggetto.jsp che non invia 'action')
            handleCreaOggetto(request, response, utente);
            return;
        }

        // Usiamo uno switch per gestire le diverse azioni
        switch (action) {
            case "delete":
                handleDeleteOggetto(request, response, utente);
                break;
            // Aggiungerai qui il case "update" quando implementerai la modifica dell'oggetto
            // case "update":
            //     handleUpdateOggetto(request, response, utente);
            //     break;
            default:
                String contenitoreId = request.getParameter("contenitoreId");
                response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&erroreOggetto=azione_non_valida");
        }
    }

    // Abbiamo bisogno di passare l'utente per i controlli di sicurezza
    private void handleCreaOggetto(HttpServletRequest request, HttpServletResponse response, Utente utente)
        throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String numeroStr = request.getParameter("numero");
        String contenitoreIdStr = request.getParameter("contenitoreId");

        if (nome != null && !nome.trim().isEmpty() &&
            numeroStr != null && !numeroStr.trim().isEmpty() &&
            contenitoreIdStr != null && !contenitoreIdStr.trim().isEmpty()) {
            try {
                int numero = Integer.parseInt(numeroStr);
                int contenitoreId = Integer.parseInt(contenitoreIdStr);

                // Aggiungiamo un controllo di sicurezza: l'utente possiede il contenitore?
                if (contenitoreDAO.getContenitoreById(contenitoreId, utente.getId()) == null) {
                    response.sendRedirect(request.getContextPath() + "/dashboard?errore=permesso_negato");
                    return;
                }

                Oggetto oggetto = new Oggetto(nome, numero, contenitoreId);
                oggettoDAO.creaOggetto(oggetto);
                response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=oggetto_creato");

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/nuovo-oggetto?contenitoreId=" + contenitoreIdStr + "&errore=numero_invalido");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/nuovo-oggetto?contenitoreId=" + contenitoreIdStr + "&errore=campi_mancanti");
        }
    }


    // NUOVO METODO PER GESTIRE L'ELIMINAZIONE
    private void handleDeleteOggetto(HttpServletRequest request, HttpServletResponse response, Utente utente)
            throws IOException {
        String contenitoreIdStr = request.getParameter("contenitoreId"); // Ci serve per il redirect
        try {
            int oggettoId = Integer.parseInt(request.getParameter("oggettoId"));
            int contenitoreId = Integer.parseInt(contenitoreIdStr);

            // --- CONTROLLO DI SICUREZZA FONDAMENTALE ---
            // Prima di eliminare, verifichiamo che l'oggetto appartenga effettivamente
            // a un contenitore di proprietà dell'utente loggato.
            Oggetto ogg = oggettoDAO.getOggettoById(oggettoId);
            if (ogg != null) {
                Contenitore cont = contenitoreDAO.getContenitoreById(ogg.getContenitoreId(), utente.getId());
                if (cont != null) {
                    // Se entrambi i controlli passano, procediamo con l'eliminazione
                    oggettoDAO.deleteOggetto(oggettoId);
                    response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&successo=Oggetto+eliminato+correttamente");
                } else {
                    // L'utente sta cercando di eliminare un oggetto che non gli appartiene
                    response.sendRedirect(request.getContextPath() + "/dashboard?errore=accesso_negato");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreId + "&errore=oggetto_non_trovato");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/oggetti?contenitoreId=" + contenitoreIdStr + "&errore=id_non_valido");
        }
    }

    // Il metodo handleCreaOggetto duplicato è stato rimosso per evitare errori.
    // Assicurati di usare solo la versione che accetta 'Utente utente'.
    // private void handleCreaOggetto(HttpServletRequest request, HttpServletResponse response)
    //     throws ServletException, IOException {
    //     ...
    // }
}