package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO {
    public List<String> listarNombresEstados() {
        List<String> estados = new ArrayList<>();
        String sql = "SELECT NombreEstado FROM EstadosEncomienda ORDER BY EstadoID";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                estados.add(rs.getString("NombreEstado"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estados;
    }
}