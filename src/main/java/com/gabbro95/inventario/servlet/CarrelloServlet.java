// File: src/main/java/com/gabbro95/inventario/servlet/CarrelloServlet.java
package com.gabbro95.inventario.servlet;

import com.gabbro95.inventario.dao.CarrelloDAO;
import com.gabbro95.inventario.dao.ContenitoreDAO; // Import aggiuntivo
import com.gabbro95.inventario.model.CarrelloItem;
import com.gabbro95.inventario.model.Contenitore; // Import aggiuntivo
import com.gabbro95.inventario.model.Utente;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // Import aggiuntivo

@WebServlet("/carrello")
public class CarrelloServlet extends HttpServlet {
    private CarrelloDAO carrelloDAO;
    private ContenitoreDAO contenitoreDAO; // Dichiarazione DAO
    // private static final int SOGLIA_CARRELLO = 5; // Questa variabile non è più strettamente usata qui, dato che la soglia viene dalla DB

    @Override
    public void init() {
        carrelloDAO = new CarrelloDAO();
        contenitoreDAO = new ContenitoreDAO(); // Inizializzazione DAO
    }

    /**
     * Mostra la pagina del carrello con opzioni di filtro per contenitore.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");

        // 1. Popola il carrello con gli oggetti sotto soglia (assicura che sia aggiornato)
        // La soglia usata qui viene dal campo soglia_minima dell'oggetto nella DB.
        carrelloDAO.popolaCarrelloAutomaticamente(utente.getId()); // La versione che svuota e ripopola

        // 2. Recupera tutti i contenitori dell'utente per la selectbox del filtro
        List<Contenitore> listaContenitori = contenitoreDAO.getContenitoriByUtenteId(utente.getId());
        request.setAttribute("listaContenitori", listaContenitori);

        // 3. Recupera gli elementi aggiornati del carrello
        List<CarrelloItem> carrelloItems = carrelloDAO.getCarrelloPerUtente(utente.getId());

        // --- Logica di filtro per contenitore ---
        String filterContenitoreIdStr = request.getParameter("filterContenitoreId");
        if (filterContenitoreIdStr != null && !filterContenitoreIdStr.isEmpty()) {
            try {
                int filterContenitoreId = Integer.parseInt(filterContenitoreIdStr);
                // Manteniamo il parametro selezionato per la JSP
                request.setAttribute("selectedContenitoreId", filterContenitoreId);

                // Ora dobbiamo filtrare `carrelloItems` in base al `contenitoreId`.
                // Per fare questo, avremmo bisogno di una modifica in CarrelloItem
                // o di un'altra query nel DAO che includa il contenitore.
                // Per semplicità e per evitare modifiche profonde al DAO esistente,
                // possiamo recuperare l'Oggetto dal DAO e poi filtrare.
                // Questa è meno efficiente ma funzionale per questa richiesta.
                // SOLUZIONE ALTERNATIVA (PIÙ EFFICIENTE):
                // Aggiungere `o.contenitore_id` alla SELECT in `getCarrelloPerUtente`
                // e un campo `contenitoreId` in `CarrelloItem`.
                // Questo è il modo migliore, ma richiede una modifica nel DAO e nel Model.

                // Per ora, useremo la soluzione attuale che filtra lato applicazione
                // richiedendo il contenitore di ogni oggetto (meno efficiente).
                // Vedere la NOTA IMPORTANTE qui sotto per la soluzione migliore.

                // NOTA IMPORTANTE: LA SOLUZIONE MIGLIORE SAREBBE MODIFICARE CarrelloDAO.getCarrelloPerUtente
                // E CarrelloItem.java per includere il contenitore_id.
                // Per mantenere le modifiche minimali al DAO che hai fornito,
                // faremo il filtro lato applicazione qui.

                // Esempio di filtro lato applicazione (richiede un DAO aggiuntivo per Oggetto):
                // Se non hai già un metodo per ottenere l'oggetto per ID o hai bisogno del contenitoreId di un oggetto
                // direttamente dalla CarrelloItem, dovresti modificare il CarrelloItem model e CarrelloDAO.
                // Supponendo di avere già un OggettoDAO disponibile o di poterlo inizializzare.
                // Per ora, assumo che tu abbia già un oggettoDAO nel tuo progetto (come in OggettoServlet).
                // Se non ce l'hai, dovrai inizializzarlo qui.

                // *** Se CarrelloItem avesse il contenitoreId, questo filtro sarebbe più semplice ***
                // Esempio: carrelloItems.stream().filter(item -> item.getContenitoreId() == filterContenitoreId).collect(Collectors.toList());

                // DATA LA STRUTTURA ATTUALE DI CarrelloItem, dobbiamo:
                // 1. O modificare CarrelloItem e CarrelloDAO per includere il contenitoreId nella query. (SOLUZIONE RACCOMANDATA)
                // 2. O fare una lookup del contenitoreId per ogni oggetto del carrello qui (MENO EFFICIENTE).

                // Per questa risposta, ti mostro la soluzione 1 (raccomandata) modificando CarrelloItem e CarrelloDAO.
                // Questo significa che dovrai applicare anche le modifiche ai file CarrelloItem.java e CarrelloDAO.java che seguiranno.

                final int fci = filterContenitoreId; // Variabile final per l'uso nella lambda
                carrelloItems = carrelloItems.stream()
                                             .filter(item -> item.getContenitoreId() == fci) // Richiede CarrelloItem.getContenitoreId()
                                             .collect(Collectors.toList());

            } catch (NumberFormatException e) {
                // Ignora o gestisci l'errore se il parametro non è un numero valido
                System.err.println("ID Contenitore di filtro non valido: " + filterContenitoreIdStr);
            }
        }

        request.setAttribute("carrelloItems", carrelloItems);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/carrello.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Gestisce l'aggiornamento degli oggetti dopo la spesa.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utente") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        Utente utente = (Utente) session.getAttribute("utente");

        String[] oggettiSelezionatiIds = request.getParameterValues("oggettoId");

        if (oggettiSelezionatiIds == null || oggettiSelezionatiIds.length == 0) {
            // Reindirizza mantenendo i parametri di filtro se presenti
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Nessun+oggetto+selezionato";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
            return;
        }

        java.util.Map<Integer, Integer> oggettiDaAggiornare = new java.util.HashMap<>();

        try {
            for (String idStr : oggettiSelezionatiIds) {
                int oggettoId = Integer.parseInt(idStr);
                String nomeParametroQuantita = "quantita_" + oggettoId;
                String quantitaStr = request.getParameter(nomeParametroQuantita);

                int quantitaDaAggiungere = Integer.parseInt(quantitaStr);

                oggettiDaAggiornare.put(oggettoId, quantitaDaAggiungere);
            }

            carrelloDAO.aggiornaOggettiESvuotaCarrello(oggettiDaAggiornare, utente.getId());
            // Reindirizza mantenendo i parametri di filtro se presenti
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?successo=Inventario+aggiornato";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Una+delle+quantita+inserite+non+e+un+numero+valido.";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            e.printStackTrace();
            String contenitoreIdParam = request.getParameter("filterContenitoreId");
            String redirectUrl = request.getContextPath() + "/carrello?errore=Errore+interno+durante+l'aggiornamento.";
            if (contenitoreIdParam != null && !contenitoreIdParam.isEmpty()) {
                redirectUrl += "&filterContenitoreId=" + contenitoreIdParam;
            }
            response.sendRedirect(redirectUrl);
        }
    }
}