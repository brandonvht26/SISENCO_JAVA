package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {

    public Empleado buscarPorUsuarioId(int usuarioId) {
        Empleado empleado = null;
        String sql = "SELECT EmpleadoID, Nombres, Apellidos FROM Empleados WHERE UsuarioID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, usuarioId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    empleado = new Empleado(
                            rs.getInt("EmpleadoID"),
                            rs.getString("Nombres"),
                            rs.getString("Apellidos")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleado;
    }

    public void crearEmpleadoYUsuario(String nombreUsuario, String clave, String nombreRol, String cedula,
                                      String nombres, String apellidos, String telefono, String correo,
                                      String direccion, int bodegaId) throws SQLException {

        String sql = "EXEC sp_CrearUsuarioEmpleado ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, clave);
            pstmt.setString(3, nombreRol);
            pstmt.setString(4, cedula);
            pstmt.setString(5, nombres);
            pstmt.setString(6, apellidos);
            pstmt.setString(7, telefono);
            pstmt.setString(8, correo);
            pstmt.setString(9, direccion);
            pstmt.setInt(10, bodegaId);

            pstmt.execute();

        } catch (SQLException e) {
            // Relanzamos la excepción para que la interfaz de usuario pueda atraparla y mostrar un mensaje
            throw new SQLException("Error al crear el usuario y empleado: " + e.getMessage(), e);
        }
    }

}