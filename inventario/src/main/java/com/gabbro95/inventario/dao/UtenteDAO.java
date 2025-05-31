package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Utente;

import java.sql.*;

public class UtenteDAO {

    public Utente trovaPerEmail(String email) {
        String sql = "SELECT * FROM utente WHERE email = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utente(rs.getInt("id"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean creaUtente(String email) {
        String sql = "INSERT INTO utente (email) VALUES (?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            // può fallire per UNIQUE constraint
            return false;
        }
    }
}
