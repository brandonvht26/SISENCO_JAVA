package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Historial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gestionar las operaciones de la tabla {@code HistorialEstados}.
 * <p>
 * Esta clase se encarga de recuperar el historial de cambios de estado para una encomienda específica,
 * permitiendo un seguimiento detallado de su trazabilidad.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class HistorialDAO {

    /**
     * Recupera el historial completo de cambios de estado para una encomienda específica.
     * <p>
     * Este método es invocado cuando el usuario hace clic en el botón "Ver Historial" en la pestaña "Gestionar Encomiendas".
     * La consulta une varias tablas para presentar información legible, como el nombre del estado y el nombre del empleado.
     *
     * @param encomiendaId El ID de la encomienda cuyo historial se desea consultar.
     * @return Una {@link List} de objetos {@link Historial}, donde cada objeto representa un cambio de estado.
     *         La lista se devuelve ordenada por fecha de cambio descendente (del más reciente al más antiguo).
     *         Retorna una lista vacía si no hay historial o si ocurre un error.
     */
    public List<Historial> obtenerPorEncomiendaId(int encomiendaId) {
        // --- MÓDULO DE INICIALIZACIÓN ---
        List<Historial> historialCompleto = new ArrayList<>();
        // Consulta SQL que une las tablas HistorialEstados, EstadosEncomienda y Empleados
        // para obtener datos descriptivos en lugar de solo IDs.
        String sql = "SELECT h.FechaCambio, es.NombreEstado, emp.Nombres + ' ' + emp.Apellidos AS NombreEmpleado, h.Observaciones " +
                "FROM HistorialEncomiendas h " +
                "JOIN EstadosEncomienda es ON h.EstadoID = es.EstadoID " +
                "JOIN Empleados emp ON h.EmpleadoResponsableID = emp.EmpleadoID " +
                "WHERE h.EncomiendaID = ? " +
                "ORDER BY h.FechaCambio DESC";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza try-with-resources para la gestión automática de recursos.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se establece el parámetro de la consulta para evitar inyección SQL.
            pstmt.setInt(1, encomiendaId);

            // Se ejecuta la consulta y se procesa el resultado.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Se itera sobre cada registro del historial.
                while (rs.next()) {
                    Historial registro = new Historial();
                    registro.setFechaCambio(rs.getTimestamp("FechaCambio").toLocalDateTime());
                    registro.setNombreEstado(rs.getString("NombreEstado"));
                    registro.setNombreEmpleado(rs.getString("NombreEmpleado"));
                    registro.setObservaciones(rs.getString("Observaciones"));

                    historialCompleto.add(registro);
                }
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            // Si ocurre un error, se imprime en la consola y se devuelve una lista vacía.
            System.err.println("Error al listar el historial de la encomienda: " + e.getMessage());
            e.printStackTrace();
        }

        // Se devuelve la lista con el historial completo.
        return historialCompleto;
    }
}