package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Historial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialDAO {
    public List<Historial> obtenerPorEncomiendaId(int encomiendaId) {
        List<Historial> historialLista = new ArrayList<>();
        String sql = "SELECT h.FechaCambio, es.NombreEstado, em.Nombres + ' ' + em.Apellidos AS Empleado, h.Observaciones " +
                "FROM HistorialEncomiendas h " +
                "JOIN EstadosEncomienda es ON h.EstadoID = es.EstadoID " +
                "JOIN Empleados em ON h.EmpleadoResponsableID = em.EmpleadoID " +
                "WHERE h.EncomiendaID = ? " +
                "ORDER BY h.FechaCambio ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, encomiendaId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Historial historial = new Historial();
                    historial.setFechaCambio(rs.getTimestamp("FechaCambio").toLocalDateTime());
                    historial.setNombreEstado(rs.getString("NombreEstado"));
                    historial.setNombreEmpleado(rs.getString("Empleado"));
                    historial.setObservaciones(rs.getString("Observaciones"));
                    historialLista.add(historial);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historialLista;
    }
}