// File: src/main/java/com/gabbro95/inventario/dao/CarrelloDAO.java
package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.CarrelloItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CarrelloDAO extends BaseDAO {

    /**
     * Popola il carrello con gli oggetti dell'utente che sono sotto una certa soglia.
     * Usa INSERT IGNORE per non inserire duplicati se un oggetto è già nel carrello.
     */
    public void popolaCarrelloAutomaticamente(int utenteId, int soglia) {
        execute(conn -> {
            String sql = "INSERT IGNORE INTO carrello (utente_id, oggetto_id) " +
                         "SELECT ?, o.id FROM oggetto o " +
                         "JOIN contenitore c ON o.contenitore_id = c.id " +
                         "WHERE c.utente_id = ? AND o.numero <= ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, utenteId);
                stmt.setInt(2, utenteId);
                stmt.setInt(3, soglia);
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Recupera tutti gli elementi nel carrello di un utente, arricchiti con i dati dell'oggetto.
     */
    public List<CarrelloItem> getCarrelloPerUtente(int utenteId) {
        return execute(conn -> {
            List<CarrelloItem> items = new ArrayList<>();
            String sql = "SELECT o.id, o.nome, o.numero FROM carrello cr " +
                         "JOIN oggetto o ON cr.oggetto_id = o.id " +
                         "WHERE cr.utente_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, utenteId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        CarrelloItem item = new CarrelloItem();
                        item.setOggettoId(rs.getInt("id"));
                        item.setOggettoNome(rs.getString("nome"));
                        item.setQuantitaAttuale(rs.getInt("numero"));
                        items.add(item);
                    }
                }
            }
            return items;
        });
    }

    /**
     * Aggiorna la quantità degli oggetti acquistati e svuota il carrello per quegli oggetti.
     */
    public void aggiornaOggettiESvuotaCarrello(java.util.Map<Integer, Integer> oggettiDaAggiornare, int utenteId) {
        if (oggettiDaAggiornare == null || oggettiDaAggiornare.isEmpty()) {
            return;
        }
        
        // Otteniamo la lista degli ID dalla mappa per la clausola DELETE
        java.util.List<Integer> oggettoIds = new ArrayList<>(oggettiDaAggiornare.keySet());

        execute(conn -> {
            conn.setAutoCommit(false); // Eseguiamo tutto in una transazione
            try {
                // Query per aggiornare la quantità
                String updateSql = "UPDATE oggetto SET numero = numero + ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    // Cicliamo sulla mappa per impostare ID e Quantità specifici
                    for (java.util.Map.Entry<Integer, Integer> entry : oggettiDaAggiornare.entrySet()) {
                        updateStmt.setInt(1, entry.getValue()); // La quantità specifica
                        updateStmt.setInt(2, entry.getKey());   // L'ID dell'oggetto
                        updateStmt.addBatch();
                    }
                    updateStmt.executeBatch();
                }

                // Query per rimuovere gli oggetti dal carrello
                String deleteSql = "DELETE FROM carrello WHERE utente_id = ? AND oggetto_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    for (Integer oggettoId : oggettoIds) {
                        deleteStmt.setInt(1, utenteId);
                        deleteStmt.setInt(2, oggettoId);
                        deleteStmt.addBatch();
                    }
                    deleteStmt.executeBatch();
                }
                
                conn.commit(); // Conferma la transazione
            } catch (Exception e) {
                conn.rollback(); // In caso di errore, annulla tutto
                throw e; // Rilancia l'eccezione
            } finally {
                conn.setAutoCommit(true);
            }
            return null;
        });
    }
    
    public void popolaCarrelloAutomaticamente(int utenteId) {
        execute(conn -> {
            // Avviamo una transazione per assicurarci che entrambe le operazioni abbiano successo
            conn.setAutoCommit(false);
            try {
                // --- PASSO 1: SVUOTA IL VECCHIO CARRELLO PER QUESTO UTENTE ---
                String deleteSql = "DELETE FROM carrello WHERE utente_id = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, utenteId);
                    deleteStmt.executeUpdate();
                }

                // --- PASSO 2: RI-POPOLA IL CARRELLO CON I DATI AGGIORNATI ---
                // Questa è la query intelligente che già avevamo
                String insertSql = "INSERT INTO carrello (utente_id, oggetto_id) " +
                                   "SELECT c.utente_id, o.id FROM oggetto o " +
                                   "JOIN contenitore c ON o.contenitore_id = c.id " +
                                   "WHERE c.utente_id = ? AND o.numero <= o.soglia_minima";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, utenteId);
                    insertStmt.executeUpdate();
                }

                conn.commit(); // Se tutto è andato bene, conferma le modifiche
            } catch (Exception e) {
                conn.rollback(); // Se c'è un errore, annulla tutto
                throw e; // Rilancia l'eccezione per farla vedere nella console
            } finally {
                conn.setAutoCommit(true); // Ripristina il comportamento di default della connessione
            }
            return null;
        });
    }
}