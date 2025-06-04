package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Oggetto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OggettoDAO extends BaseDAO {

    public List<Oggetto> trovaPerContenitoreId(int contenitoreId) {
        return execute(conn -> {
            List<Oggetto> oggetti = new ArrayList<>();
            String sql = "SELECT * FROM oggetto WHERE contenitore_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Oggetto o = new Oggetto();
                        o.setId(rs.getInt("id"));
                        o.setNome(rs.getString("nome"));
                        o.setNumero(rs.getInt("numero"));
                        o.setDataInserimento(rs.getDate("data_inserimento").toLocalDate());
                        o.setContenitoreId(contenitoreId);
                        oggetti.add(o);
                    }
                }
            }
            return oggetti;
        });
    }

    public void creaOggetto(Oggetto oggetto) {
        execute(conn -> {
            String sql = "INSERT INTO oggetto (nome, numero, data_inserimento, contenitore_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, oggetto.getNome());
                stmt.setInt(2, oggetto.getNumero());
                stmt.setDate(3, java.sql.Date.valueOf(oggetto.getDataInserimento()));
                stmt.setInt(4, oggetto.getContenitoreId());
                stmt.executeUpdate();
            }
            return null;
        });
    }
}
