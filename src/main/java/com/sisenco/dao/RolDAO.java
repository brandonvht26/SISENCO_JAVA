package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gestionar las operaciones de la tabla {@code Roles}.
 * <p>
 * Esta clase se encarga de recuperar la lista de roles disponibles en el sistema,
 * como "ADMINISTRADOR" y "OPERADOR", para ser utilizados en la interfaz de usuario.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class RolDAO {

    /**
     * Recupera una lista con los nombres de todos los roles disponibles en la base de datos.
     * <p>
     * Este método es utilizado en el formulario "Gestionar Usuarios" para poblar el ComboBox
     * que permite asignar un rol a un nuevo empleado al momento de su creación.
     *
     * @return Una {@link List} de {@code String} con los nombres de los roles.
     *         Retorna una lista vacía si ocurre un error o no hay roles definidos.
     */
    public List<String> listarNombresRoles() {
        // --- MÓDULO DE INICIALIZACIÓN ---
        List<String> roles = new ArrayList<>();
        // Se ordena alfabéticamente para una presentación consistente en el ComboBox.
        String sql = "SELECT NombreRol FROM Roles ORDER BY NombreRol";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza try-with-resources para la gestión automática de recursos.
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Se itera sobre cada fila del resultado.
            while (rs.next()) {
                // Se añade el nombre del rol a la lista.
                roles.add(rs.getString("NombreRol"));
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            // Si ocurre un error, se imprime en la consola para diagnóstico.
            // El método devolverá una lista vacía, un comportamiento seguro para la UI.
            System.err.println("Error al listar los roles: " + e.getMessage());
            e.printStackTrace();
        }
        // Se devuelve la lista de roles.
        return roles;
    }
}