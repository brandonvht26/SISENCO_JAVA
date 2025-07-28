package com.sisenco.ui;

import com.sisenco.dao.EncomiendaDAO;
import com.sisenco.dao.EstadoDAO;
import com.sisenco.model.Encomienda;
import com.sisenco.util.ExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FormReportes extends JPanel {
    private JPanel panelPrincipal;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JComboBox<String> comboEstado;
    private JButton btnGenerarReporte;
    private JTable tablaReporte;
    private JButton btnExportar;
    private JTable JScroll;

    private EncomiendaDAO encomiendaDAO;
    private EstadoDAO estadoDAO;

    public FormReportes() {
        add(panelPrincipal);
        encomiendaDAO = new EncomiendaDAO();
        estadoDAO = new EstadoDAO();

        cargarEstados();

        btnGenerarReporte.addActionListener(e -> generarReporte());
        btnExportar.addActionListener(e -> exportarReporte());
    }

    private void cargarEstados() {
        comboEstado.addItem("Todos"); // Opción para no filtrar por estado
        estadoDAO.listarNombresEstados().forEach(comboEstado::addItem);
    }

    private void exportarReporte() {
        if (tablaReporte.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos en el reporte para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        ExcelExporter.exportToExcel(tablaReporte, this);
    }

    private void generarReporte() {
        String fechaInicio = txtFechaInicio.getText();
        String fechaFin = txtFechaFin.getText();
        String estado = (String) comboEstado.getSelectedItem();

        // Validación simple
        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Las fechas de inicio y fin son obligatorias.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Encomienda> resultados = encomiendaDAO.generarReporte(estado, fechaInicio, fechaFin);

        // Poblar la tabla con los resultados
        String[] columnas = {"ID", "Fecha Ingreso", "Remitente", "Destinatario", "Descripción", "Estado"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);

        for (Encomienda enc : resultados) {
            model.addRow(new Object[]{
                    enc.getEncomiendaID(),
                    enc.getFechaIngreso().toLocalDate(),
                    enc.getClienteRemitente().getNombres(), // Solo mostramos el nombre
                    enc.getNombreDestinatario(),
                    enc.getDescripcionPaquete(),
                    enc.getEstadoActual()
            });
        }
        tablaReporte.setModel(model);
    }
}