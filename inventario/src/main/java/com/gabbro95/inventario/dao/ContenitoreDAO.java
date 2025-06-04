package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Contenitore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContenitoreDAO extends BaseDAO {

    public List<Contenitore> getContenitoriPerUtente(String emailUtente) {
        return execute(conn -> {
            List<Contenitore> contenitori = new ArrayList<>();
            String sql = "SELECT * FROM contenitore WHERE email_utente = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, emailUtente);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Contenitore c = new Contenitore();
                        c.setId(rs.getInt("id"));
                        c.setNome(rs.getString("nome"));
                        c.setEmailUtente(emailUtente);
                        contenitori.add(c);
                    }
                }
            }
            return contenitori;
        });
    }

    public void creaContenitore(String nome, String emailUtente) {
        execute(conn -> {
            String sql = "INSERT INTO contenitore (nome, email_utente) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, emailUtente);
                stmt.executeUpdate();
            }
            return null;
        });
    }

    public void eliminaContenitore(int contenitoreId) {
        execute(conn -> {
            String sql = "DELETE FROM contenitore WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                stmt.executeUpdate();
            }
            return null;
        });
    }
}
