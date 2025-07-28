package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Valida las credenciales de un usuario.
     * @param nombreUsuario El nombre de usuario.
     * @param clave La contraseña en texto plano.
     * @return un objeto Usuario si las credenciales son correctas y el usuario está activo, de lo contrario null.
     */
    public Usuario validarUsuario(String nombreUsuario, String clave) {
        Usuario usuario = null;
        // La consulta compara el hash de la clave ingresada con el hash almacenado.
        String sql = "SELECT U.*, R.NombreRol FROM Usuarios U " +
                "JOIN Roles R ON U.RolID = R.RolID " +
                "WHERE U.NombreUsuario = ? AND U.ClaveHash = HASHBYTES('SHA2_256', CAST(? AS VARCHAR(100))) AND U.Estado = 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, clave); // Pasamos la clave en texto plano, SQL Server la hashea

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUsuarioID(rs.getInt("UsuarioID"));
                    usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                    usuario.setRol(rs.getString("NombreRol"));
                    usuario.setActivo(rs.getBoolean("Estado"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return usuario;
    }
}