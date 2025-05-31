package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Oggetto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OggettoDAO {

    public List<Oggetto> trovaPerContenitoreId(int contenitoreId) {
        List<Oggetto> lista = new ArrayList<>();
        String sql = "SELECT * FROM oggetto WHERE contenitore_id = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contenitoreId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Oggetto o = new Oggetto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("numero"),
                    rs.getDate("data_inserimento").toLocalDate(),
                    rs.getInt("contenitore_id")
                );
                lista.add(o);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean creaOggetto(Oggetto oggetto) {
        String sql = "INSERT INTO oggetto (nome, numero, data_inserimento, contenitore_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, oggetto.getNome());
            stmt.setInt(2, oggetto.getNumero());
            stmt.setDate(3, java.sql.Date.valueOf(oggetto.getDataInserimento()));
            stmt.setInt(4, oggetto.getContenitoreId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    oggetto.setId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
