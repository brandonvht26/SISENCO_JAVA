package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BodegaDAO {
    // Usamos un Map para guardar el nombre y el ID de la bodega
    public Map<String, Integer> listarBodegas() {
        Map<String, Integer> bodegas = new HashMap<>();
        String sql = "SELECT BodegaID, NombreBodega FROM Bodegas";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bodegas.put(rs.getString("NombreBodega"), rs.getInt("BodegaID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bodegas;
    }
}