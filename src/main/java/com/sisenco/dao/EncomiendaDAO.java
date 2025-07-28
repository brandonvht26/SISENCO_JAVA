package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Cliente;
import com.sisenco.model.Empleado;
import com.sisenco.model.Encomienda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EncomiendaDAO {

    public void registrar(Encomienda encomienda) {
        String sql = "EXEC sp_RegistrarEncomienda ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, encomienda.getClienteRemitente().getCedula());
            pstmt.setInt(2, encomienda.getEmpleadoRegistra().getEmpleadoID());
            pstmt.setInt(3, 1); // Asumimos Bodega Origen ID = 1 por ahora
            pstmt.setString(4, encomienda.getNombreDestinatario());
            pstmt.setString(5, encomienda.getDireccionDestino());
            pstmt.setString(6, encomienda.getTelefonoDestino());
            pstmt.setString(7, encomienda.getDescripcionPaquete());
            pstmt.setDouble(8, encomienda.getPesoKG());

            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Añadir este método dentro de la clase EncomiendaDAO.java

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
            e.printStackTrace();
        }
    }

    public List<Encomienda> generarReporte(String estadoNombre, String fechaInicio, String fechaFin) {
        List<Encomienda> reporte = new ArrayList<>();
        // Usamos el nombre del SP que creamos en el Módulo 5
        String sql = "EXEC sp_ReporteEncomiendas ?, ?, ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Si el estado es "Todos", pasamos NULL al SP, de lo contrario pasamos el nombre.
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

                    // Creamos un cliente temporal solo con el nombre para el reporte
                    Cliente remitente = new Cliente();
                    remitente.setNombres(rs.getString("Remitente"));
                    enc.setClienteRemitente(remitente);

                    enc.setNombreDestinatario(rs.getString("Destinatario"));
                    enc.setDescripcionPaquete(rs.getString("DescripcionPaquete"));
                    enc.setEstadoActual(rs.getString("EstadoActual"));
                    // El SP también devuelve 'DiasTranscurridos', pero lo omitimos en el modelo por simplicidad

                    reporte.add(enc);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reporte;
    }

    // Este es un ejemplo. En tu aplicación final, el reporte vendrá del SP sp_ReporteEncomiendas
    public List<Encomienda> listarTodas() {
        List<Encomienda> lista = new ArrayList<>();
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
            e.printStackTrace();
        }
        return lista;
    }
}