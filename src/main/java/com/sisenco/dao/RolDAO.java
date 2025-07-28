package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    public List<String> listarNombresRoles() {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT NombreRol FROM Roles";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roles.add(rs.getString("NombreRol"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
}