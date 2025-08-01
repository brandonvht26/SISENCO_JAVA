package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gestionar las operaciones de la tabla {@code EstadosEncomienda}.
 * <p>
 * Esta clase se especializa en recuperar la lista de posibles estados por los que puede pasar una encomienda,
 * como "Ingresado", "En Tránsito", "Entregado", etc.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class EstadoDAO {

    /**
     * Recupera una lista con los nombres de todos los estados de encomienda disponibles.
     * <p>
     * Este método es crucial para poblar los ComboBox en la interfaz de usuario, permitiendo
     * al usuario seleccionar un estado de una lista predefinida y consistente. Se utiliza en:
     * <ul>
     *     <li>El formulario de "Reportes" para filtrar por estado.</li>
     *     <li>El diálogo de "Actualizar Estado" para elegir el nuevo estado de una encomienda.</li>
     * </ul>
     * La consulta ordena los estados por su ID para mantener un orden lógico (ej. Ingresado -> En Tránsito -> Entregado).
     *
     * @return Una {@link List} de {@code String} con los nombres de los estados.
     *         Retorna una lista vacía si ocurre un error o no hay estados definidos.
     */
    public List<String> listarNombresEstados() {
        // --- MÓDULO DE INICIALIZACIÓN ---
        // Se crea una lista para almacenar los nombres de los estados.
        List<String> estados = new ArrayList<>();
        // Se define la consulta SQL. Se ordena por EstadoID para asegurar un orden consistente y lógico.
        String sql = "SELECT NombreEstado FROM EstadosEncomienda ORDER BY EstadoID";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza try-with-resources para la gestión automática de recursos (conexión, statement, resultset).
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Se itera sobre cada fila del resultado.
            while (rs.next()) {
                // Se añade el nombre del estado a la lista.
                estados.add(rs.getString("NombreEstado"));
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            // Si ocurre un error, se imprime en la consola para diagnóstico.
            // La aplicación no se detiene y el método devolverá una lista vacía,
            // lo que resulta en un ComboBox sin elementos, un comportamiento seguro.
            System.err.println("Error al listar los nombres de los estados: " + e.getMessage());
            e.printStackTrace();
        }
        // Se devuelve la lista de estados (o una lista vacía si hubo un error).
        return estados;
    }
}