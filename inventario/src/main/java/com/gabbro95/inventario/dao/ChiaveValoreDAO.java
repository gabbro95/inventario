package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.ChiaveValore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ChiaveValoreDAO extends BaseDAO {

    public Map<Integer, List<ChiaveValore>> getChiaviPerOggetti(List<Integer> oggettoIds) {
        return execute(conn -> {
            Map<Integer, List<ChiaveValore>> mappa = new HashMap<>();

            if (oggettoIds.isEmpty()) return mappa;

            String placeholders = String.join(",", Collections.nCopies(oggettoIds.size(), "?"));
            String sql = "SELECT * FROM chiave_valore WHERE oggetto_id IN (" + placeholders + ")";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < oggettoIds.size(); i++) {
                    stmt.setInt(i + 1, oggettoIds.get(i));
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int oggettoId = rs.getInt("oggetto_id");
                        ChiaveValore kv = new ChiaveValore(
                            rs.getString("chiave"),
                            rs.getString("valore"),
                            oggettoId
                        );
                        mappa.computeIfAbsent(oggettoId, k -> new ArrayList<>()).add(kv);
                    }
                }
            }
            return mappa;
        });
    }

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
}