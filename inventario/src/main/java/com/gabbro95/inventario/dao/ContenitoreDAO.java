package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Contenitore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContenitoreDAO {

    public List<Contenitore> trovaPerEmailUtente(String email) {
        List<Contenitore> lista = new ArrayList<>();
        String sql = "SELECT * FROM contenitore WHERE email_utente = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Contenitore c = new Contenitore(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email_utente")
                );
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean creaContenitore(String nome, String emailUtente) {
        String sql = "INSERT INTO contenitore (nome, email_utente) VALUES (?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, emailUtente);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
