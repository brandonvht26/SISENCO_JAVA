package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) para gestionar las operaciones de autenticación de la entidad {@link Usuario}.
 * <p>
 * Esta clase es responsable de validar las credenciales de un usuario contra la base de datos
 * de una manera segura, sin manejar contraseñas en texto plano directamente en la lógica de comparación.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales de un usuario contra la base de datos.
     * <p>
     * Este método implementa una validación segura:
     * 1. No trae la contraseña hasheada desde la base de datos a la aplicación.
     * 2. Envía la contraseña en texto plano al servidor de base de datos, que se encarga de
     *    hashearla (usando SHA2_256) y compararla con el hash almacenado.
     * 3. Adicionalmente, verifica que el usuario esté activo ({@code Estado = 1}).
     * <p>
     * Esta es la lógica que se ejecuta cuando el usuario presiona "Ingresar" en el {@link com.sisenco.ui.FormLogin}.
     *
     * @param nombreUsuario El nombre de usuario a validar.
     * @param clave La contraseña en texto plano ingresada por el usuario.
     * @return Un objeto {@link Usuario} completamente poblado (con ID, nombre y rol) si las credenciales son
     *         correctas y el usuario está activo. De lo contrario, retorna {@code null}.
     */
    public Usuario validarUsuario(String nombreUsuario, String clave) {
        // --- MÓDULO DE INICIALIZACIÓN ---
        Usuario usuario = null;
        // La consulta SQL une Usuarios y Roles para obtener el nombre del rol.
        // La parte crucial es HASHBYTES('SHA2_256', ...), que calcula el hash de la clave
        // proporcionada y lo compara con el hash almacenado en la columna ClaveHash.
        String sql = "SELECT U.*, R.NombreRol FROM Usuarios U " +
                "JOIN Roles R ON U.RolID = R.RolID " +
                "WHERE U.NombreUsuario = ? AND U.ClaveHash = HASHBYTES('SHA2_256', CAST(? AS VARCHAR(100))) AND U.Estado = 1";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se establecen los parámetros para la consulta segura.
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, clave); // La clave se envía en texto plano; SQL Server se encarga de hashearla.

            // Se ejecuta la consulta.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Si rs.next() es verdadero, significa que la consulta encontró una coincidencia.
                // Esto implica que el usuario, la clave hasheada y el estado activo coincidieron.
                if (rs.next()) {
                    // Se crea y puebla el objeto Usuario con los datos recuperados.
                    usuario = new Usuario();
                    usuario.setUsuarioID(rs.getInt("UsuarioID"));
                    usuario.setNombreUsuario(rs.getString("NombreUsuario"));
                    usuario.setRol(rs.getString("NombreRol"));
                    usuario.setActivo(rs.getBoolean("Estado"));
                }
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            System.err.println("Error al validar usuario: " + e.getMessage());
            e.printStackTrace();
        }
        // Se devuelve el objeto usuario (que será null si la validación falló).
        return usuario;
    }
}