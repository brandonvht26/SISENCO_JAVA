package com.sisenco.ui;

import com.sisenco.dao.ClienteDAO;
import com.sisenco.model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FormClientes extends JPanel { // Asegúrate que diga extends JPanel
    private JPanel panelPrincipal;
    private JTextField txtId;
    private JTextField txtCedula;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtTelefono;
    private JTextField txtDireccion;
    private JButton btnBuscar;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JTable tablaClientes;
    private JButton btnLimpiar;
    private JScrollPane scrollPane;

    private ClienteDAO clienteDAO;

    public FormClientes() {
        // ESTA LÍNEA ES EL AJUSTE CLAVE:
        // Le dice a este JPanel que su contenido es el panel que diseñamos en el .form
        add(panelPrincipal);

        // El resto del código es el mismo que ya tenías
        clienteDAO = new ClienteDAO();
        cargarClientesEnTabla();

        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnGuardar.addActionListener(e -> guardarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());

        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaClientes.getSelectedRow() != -1) {
                int filaSeleccionada = tablaClientes.getSelectedRow();
                txtId.setText(tablaClientes.getValueAt(filaSeleccionada, 0).toString());
                txtCedula.setText(tablaClientes.getValueAt(filaSeleccionada, 1).toString());
                txtNombres.setText(tablaClientes.getValueAt(filaSeleccionada, 2).toString());
                txtApellidos.setText(tablaClientes.getValueAt(filaSeleccionada, 3).toString());
                txtTelefono.setText(tablaClientes.getValueAt(filaSeleccionada, 4).toString());
                txtDireccion.setText(tablaClientes.getValueAt(filaSeleccionada, 5).toString());
            }
        });
    }

    // Ya no necesitamos el método getPanelPrincipal()

    // --- El resto de los métodos (cargarClientesEnTabla, etc.) se mantienen igual ---
    private void cargarClientesEnTabla() {
        String[] columnas = {"ID", "Cédula", "Nombres", "Apellidos", "Teléfono", "Dirección"};
        DefaultTableModel tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        List<Cliente> clientes = clienteDAO.listarTodos();
        for (Cliente cliente : clientes) {
            Object[] fila = { cliente.getClienteID(), cliente.getCedula(), cliente.getNombres(), cliente.getApellidos(), cliente.getTelefono(), cliente.getDireccion() };
            tableModel.addRow(fila);
        }
        tablaClientes.setModel(tableModel);
    }
    private void limpiarCampos() {
        txtId.setText("");
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
        tablaClientes.clearSelection();
    }
    private void guardarCliente() {
        if (txtCedula.getText().isEmpty() || txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cédula, Nombres y Apellidos son obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Cliente cliente = new Cliente();
        cliente.setCedula(txtCedula.getText());
        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setDireccion(txtDireccion.getText());
        if (!txtId.getText().isEmpty()) {
            cliente.setClienteID(Integer.parseInt(txtId.getText()));
        }
        clienteDAO.guardar(cliente);
        JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        cargarClientesEnTabla();
    }
    private void buscarCliente() {
        String cedula = txtCedula.getText();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula para buscar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Cliente cliente = clienteDAO.buscarPorCedula(cedula);
        if (cliente != null) {
            txtId.setText(String.valueOf(cliente.getClienteID()));
            txtNombres.setText(cliente.getNombres());
            txtApellidos.setText(cliente.getApellidos());
            txtTelefono.setText(cliente.getTelefono());
            txtDireccion.setText(cliente.getDireccion());
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con esa cédula.", "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        }
    }
    private void eliminarCliente() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla o busque uno para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            clienteDAO.eliminar(id);
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarClientesEnTabla();
        }
    }
}