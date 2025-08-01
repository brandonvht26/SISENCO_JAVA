package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Cliente;
import com.sisenco.model.Empleado;
import com.sisenco.model.Encomienda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para gestionar todas las operaciones de la entidad {@link Encomienda}.
 * <p>
 * Esta clase es el núcleo de la lógica de negocio, interactuando con la base de datos
 * principalmente a través de procedimientos almacenados para garantizar la integridad y consistencia de los datos.
 * Maneja el registro, la actualización de estado y la generación de reportes.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class EncomiendaDAO {

    /**
     * Registra una nueva encomienda en el sistema ejecutando el procedimiento almacenado {@code sp_RegistrarEncomienda}.
     * <p>
     * El procedimiento almacenado se encarga de crear el registro en la tabla {@code Encomiendas} y, crucialmente,
     * también inserta el primer registro en la tabla {@code HistorialEstados} con el estado "Ingresado".
     * Esto asegura que cada encomienda nazca con un historial válido.
     * Esta funcionalidad se ve en acción en el video: https://youtu.be/aaHpGQKzgvk?t=75
     *
     * @param encomienda El objeto {@link Encomienda} con todos los datos necesarios para el registro.
     */
    public void registrar(Encomienda encomienda) {
        // Se define la llamada al procedimiento almacenado.
        String sql = "EXEC sp_RegistrarEncomienda ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se establecen los parámetros del procedimiento.
            pstmt.setString(1, encomienda.getClienteRemitente().getCedula());
            pstmt.setInt(2, encomienda.getEmpleadoRegistra().getEmpleadoID());
            pstmt.setInt(3, 1); // NOTA: Se asume Bodega Origen ID = 1. En una versión futura, esto podría ser dinámico.
            pstmt.setString(4, encomienda.getNombreDestinatario());
            pstmt.setString(5, encomienda.getDireccionDestino());
            pstmt.setString(6, encomienda.getTelefonoDestino());
            pstmt.setString(7, encomienda.getDescripcionPaquete());
            pstmt.setDouble(8, encomienda.getPesoKG());

            // Se ejecuta el procedimiento. No se espera un ResultSet.
            pstmt.execute();

        } catch (SQLException e) {
            // En una aplicación real, sería ideal relanzar una excepción personalizada o usar un logger.
            System.err.println("Error al registrar la encomienda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el estado de una encomienda existente ejecutando el procedimiento almacenado {@code sp_ActualizarEstadoEncomienda}.
     * <p>
     * Este método es fundamental para el seguimiento del paquete. El procedimiento almacenado actualiza el {@code EstadoID}
     * en la tabla {@code Encomiendas} y, al mismo tiempo, añade una nueva fila en {@code HistorialEstados}
     * para registrar el cambio, quién lo hizo y cuándo.
     *
     * @param encomiendaId El ID de la encomienda a actualizar.
     * @param nuevoEstado El nombre del nuevo estado (ej. "En Tránsito").
     * @param empleadoResponsableId El ID del empleado que realiza la actualización.
     * @param observaciones Notas adicionales sobre la actualización de estado.
     */
    public void actualizarEstado(int encomiendaId, String nuevoEstado, int empleadoResponsableId, String observaciones) {
        String sql = "EXEC sp_ActualizarEstadoEncomienda ?, ?, ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, encomiendaId);
            pstmt.setString(2, nuevoEstado);
            pstmt.setInt(3, empleadoResponsableId);
            pstmt.setString(4, observaciones);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de la encomienda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Genera un reporte de encomiendas filtrado por estado y rango de fechas usando el SP {@code sp_ReporteEncomiendas}.
     * <p>
     * Este método alimenta la pestaña de "Reportes", permitiendo al administrador generar vistas personalizadas
     *
     * @param estadoNombre El nombre del estado para filtrar. Si es "Todos", se buscan todos los estados.
     * @param fechaInicio La fecha de inicio del rango de búsqueda (formato 'YYYY-MM-DD').
     * @param fechaFin La fecha de fin del rango de búsqueda (formato 'YYYY-MM-DD').
     * @return Una {@link List} de objetos {@link Encomienda} que coinciden con los criterios de búsqueda.
     */
    public List<Encomienda> generarReporte(String estadoNombre, String fechaInicio, String fechaFin) {
        List<Encomienda> reporte = new ArrayList<>();
        String sql = "EXEC sp_ReporteEncomiendas ?, ?, ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Lógica para manejar el filtro "Todos": se pasa NULL al procedimiento almacenado.
            if ("Todos".equals(estadoNombre)) {
                pstmt.setNull(1, Types.VARCHAR);
            } else {
                pstmt.setString(1, estadoNombre);
            }
            pstmt.setString(2, fechaInicio);
            pstmt.setString(3, fechaFin);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Encomienda enc = new Encomienda();
                    enc.setEncomiendaID(rs.getInt("EncomiendaID"));
                    enc.setFechaIngreso(rs.getTimestamp("FechaIngreso").toLocalDateTime());

                    // Para el reporte, creamos un objeto Cliente simplificado solo con el nombre completo.
                    Cliente remitente = new Cliente();
                    remitente.setNombres(rs.getString("Remitente")); // El SP ya concatena nombre y apellido.
                    enc.setClienteRemitente(remitente);

                    enc.setNombreDestinatario(rs.getString("Destinatario"));
                    enc.setDescripcionPaquete(rs.getString("DescripcionPaquete"));
                    enc.setEstadoActual(rs.getString("EstadoActual"));
                    // El SP también devuelve 'DiasTranscurridos', pero se omite en el modelo Java por simplicidad.

                    reporte.add(enc);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al generar el reporte de encomiendas: " + e.getMessage());
            e.printStackTrace();
        }
        return reporte;
    }

    /**
     * Recupera una lista simplificada de todas las encomiendas para mostrar en la tabla principal.
     * <p>
     * A diferencia de {@code generarReporte}, este método no usa un SP y realiza un {@code JOIN} simple
     * para obtener los datos más relevantes y recientes para la vista general en la pestaña "Gestionar Encomiendas".
     *
     * @return Una {@link List} de objetos {@link Encomienda} con datos básicos.
     */
    public List<Encomienda> listarTodas() {
        List<Encomienda> lista = new ArrayList<>();
        // Consulta que une Encomiendas, Clientes y Estados para obtener nombres legibles.
        String sql = "SELECT e.EncomiendaID, e.FechaIngreso, c.ClienteID, c.Nombres as ClienteNombres, c.Apellidos as ClienteApellidos, " +
                "e.NombreDestinatario, es.NombreEstado FROM Encomiendas e " +
                "JOIN Clientes c ON e.ClienteRemitenteID = c.ClienteID " +
                "JOIN EstadosEncomienda es ON e.EstadoID = es.EstadoID ORDER BY e.FechaIngreso DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while(rs.next()) {
                Encomienda enc = new Encomienda();
                enc.setEncomiendaID(rs.getInt("EncomiendaID"));
                enc.setFechaIngreso(rs.getTimestamp("FechaIngreso").toLocalDateTime());

                // Se construye el objeto Cliente completo para tener todos sus datos disponibles.
                Cliente cliente = new Cliente();
                cliente.setClienteID(rs.getInt("ClienteID"));
                cliente.setNombres(rs.getString("ClienteNombres"));
                cliente.setApellidos(rs.getString("ClienteApellidos"));
                enc.setClienteRemitente(cliente);

                enc.setNombreDestinatario(rs.getString("NombreDestinatario"));
                enc.setEstadoActual(rs.getString("NombreEstado"));

                lista.add(enc);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar todas las encomiendas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }
}