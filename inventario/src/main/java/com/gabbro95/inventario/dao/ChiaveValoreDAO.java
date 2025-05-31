package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.ChiaveValore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiaveValoreDAO {

    public List<ChiaveValore> trovaPerOggettoId(int oggettoId) {
        List<ChiaveValore> lista = new ArrayList<>();
        String sql = "SELECT * FROM chiave_valore WHERE oggetto_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, oggettoId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ChiaveValore cv = new ChiaveValore(
                    rs.getInt("id"),
                    rs.getString("chiave"),
                    rs.getString("valore"),
                    rs.getInt("oggetto_id")
                );
                lista.add(cv);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean creaChiaveValore(ChiaveValore cv) {
        String sql = "INSERT INTO chiave_valore (chiave, valore, oggetto_id) VALUES (?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cv.getChiave());
            stmt.setString(2, cv.getValore());
            stmt.setInt(3, cv.getOggettoId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
