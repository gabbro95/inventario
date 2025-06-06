package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.ChiaveValore;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChiaveValoreDAO extends BaseDAO {

    /**
     * Inserisce un nuovo dettaglio (chiave-valore) nel database.
     * Questo risolve l'errore "aggiungiChiaveValore(...) is undefined".
     * Ho rinominato il metodo e messo i parametri nell'ordine corretto.
     */
    public void aggiungiChiaveValore(int oggettoId, String chiave, String valore) {
        execute(conn -> {
            String sql = "INSERT INTO chiave_valore (oggetto_id, chiave, valore) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, oggettoId);
                stmt.setString(2, chiave);
                stmt.setString(3, valore);
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Recupera tutti i dettagli per una lista di ID di oggetti.
     * Questo risolve l'errore "getChiaviPerOggetti(...) is undefined".
     * Restituisce una Mappa dove la chiave è l'ID dell'oggetto e il valore è la lista dei suoi dettagli.
     */
    public Map<Integer, List<ChiaveValore>> getChiaviPerOggetti(List<Integer> oggettoIds) {
        if (oggettoIds == null || oggettoIds.isEmpty()) {
            return new HashMap<>(); // Restituisce una mappa vuota se non ci sono ID
        }

        return execute(conn -> {
            Map<Integer, List<ChiaveValore>> mappaRisultati = new HashMap<>();
            
            // Crea una stringa di placeholder "(?, ?, ?...)" per la clausola IN
            String placeholders = oggettoIds.stream().map(id -> "?").collect(Collectors.joining(", "));
            String sql = "SELECT * FROM chiave_valore WHERE oggetto_id IN (" + placeholders + ")";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Imposta tutti gli ID nella query
                for (int i = 0; i < oggettoIds.size(); i++) {
                    stmt.setInt(i + 1, oggettoIds.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ChiaveValore kv = new ChiaveValore();
                        kv.setId(rs.getInt("id"));
                        kv.setChiave(rs.getString("chiave"));
                        kv.setValore(rs.getString("valore"));
                        kv.setOggettoId(rs.getInt("oggetto_id"));

                        // Aggiunge la chiave-valore alla lista corretta nella mappa
                        mappaRisultati.computeIfAbsent(kv.getOggettoId(), k -> new ArrayList<>()).add(kv);
                    }
                }
            }
            return mappaRisultati;
        });
    }
    
    public void deleteChiaveValore(int id) {
        execute(conn -> {
            String sql = "DELETE FROM chiave_valore WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            return null;
        });
    }
    
    public ChiaveValore getChiaveValoreById(int id) {
        return execute(conn -> {
            String sql = "SELECT * FROM chiave_valore WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        ChiaveValore kv = new ChiaveValore();
                        kv.setId(rs.getInt("id"));
                        kv.setChiave(rs.getString("chiave"));
                        kv.setValore(rs.getString("valore"));
                        kv.setOggettoId(rs.getInt("oggetto_id"));
                        return kv;
                    }
                }
            }
            return null;
        });
    }

    public void updateChiaveValore(ChiaveValore kv) {
        execute(conn -> {
            String sql = "UPDATE chiave_valore SET chiave = ?, valore = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, kv.getChiave());
                stmt.setString(2, kv.getValore());
                stmt.setInt(3, kv.getId());
                stmt.executeUpdate();
            }
            return null;
        });
    }
}