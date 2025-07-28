package com.sisenco.ui;

import com.sisenco.dao.HistorialDAO;
import com.sisenco.model.Historial;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FormHistorial extends JDialog {
    private JPanel panelHistorial;
    private JTable tablaHistorial;
    private JLabel lblTitulo;
    private JScrollPane scrollPane;

    public FormHistorial(Frame parent, int encomiendaId) {
        super(parent, "Historial de Encomienda", true);
        setContentPane(panelHistorial);
        setSize(600, 400);
        setLocationRelativeTo(parent);

        lblTitulo.setText("Trazabilidad de la Encomienda ID: " + encomiendaId);

        cargarHistorialEnTabla(encomiendaId);
    }

    private void cargarHistorialEnTabla(int encomiendaId) {
        String[] columnas = {"Fecha y Hora", "Nuevo Estado", "Responsable", "Observaciones"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        HistorialDAO historialDAO = new HistorialDAO();
        List<Historial> lista = historialDAO.obtenerPorEncomiendaId(encomiendaId);

        for (Historial item : lista) {
            model.addRow(new Object[]{
                    item.getFechaCambio(),
                    item.getNombreEstado(),
                    item.getNombreEmpleado(),
                    item.getObservaciones()
            });
        }
        tablaHistorial.setModel(model);
    }
}