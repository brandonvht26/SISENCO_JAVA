package com.sisenco.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.sisenco.dao.ClienteDAO;
import com.sisenco.dao.EmpleadoDAO;
import com.sisenco.dao.EncomiendaDAO;
import com.sisenco.dao.EstadoDAO; // Importar el nuevo DAO
import com.sisenco.model.Cliente;
import com.sisenco.model.Empleado;
import com.sisenco.model.Encomienda;
import com.sisenco.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FormEncomiendas extends JPanel {
    // ... (declaraciones de componentes existentes)
    private JButton btnActualizarEstado; // Declarar el nuevo botón
    private JComboBox comboClientes;
    private JPanel panelEncomiendas;
    private JTextField txtDestinatarioNombre;
    private JTextField txtDestinatarioDireccion;
    private JTextField txtDestinatarioTelefono;
    private JTextField txtPeso;
    private JTable tablaEncomiendas;
    private JTextArea txtAreaDescripcion;
    private JButton btnLimpiar;
    private JButton btnRegistrar;
    private JButton btnVerHistorial;
    private ClienteDAO clienteDAO;
    private EncomiendaDAO encomiendaDAO;
    private EmpleadoDAO empleadoDAO;
    private EstadoDAO estadoDAO; // Declarar el nuevo DAO
    private Usuario usuarioLogueado;
    private Empleado empleadoLogueado;

    public FormEncomiendas(Usuario usuario) {
        this.usuarioLogueado = usuario;
        add(panelEncomiendas);

        clienteDAO = new ClienteDAO();
        encomiendaDAO = new EncomiendaDAO();
        empleadoDAO = new EmpleadoDAO();
        estadoDAO = new EstadoDAO(); // Inicializar el nuevo DAO

        this.empleadoLogueado = empleadoDAO.buscarPorUsuarioId(usuario.getUsuarioID());

        cargarClientesEnComboBox();
        cargarEncomiendasEnTabla();

        btnRegistrar.addActionListener(e -> registrarEncomienda());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Listener para el nuevo botón
        btnActualizarEstado.addActionListener(e -> mostrarDialogoActualizarEstado());
        btnVerHistorial.addActionListener(e -> verHistorial());

        txtDestinatarioNombre.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre");
        txtDestinatarioDireccion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Dirección");
        txtDestinatarioTelefono.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Teléfono");
        txtPeso.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Peso");
        txtAreaDescripcion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Descripción");
    }

    private void mostrarDialogoActualizarEstado() {
        int filaSeleccionada = tablaEncomiendas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una encomienda de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtenemos los datos de la fila seleccionada
        int encomiendaId = (int) tablaEncomiendas.getValueAt(filaSeleccionada, 0);
        String estadoActual = (String) tablaEncomiendas.getValueAt(filaSeleccionada, 3);

        // Creamos un panel personalizado para el diálogo
        JPanel panelDialogo = new JPanel();
        panelDialogo.setLayout(new BoxLayout(panelDialogo, BoxLayout.Y_AXIS));

        // ComboBox con los posibles nuevos estados
        List<String> estadosPosibles = estadoDAO.listarNombresEstados();
        JComboBox<String> comboEstados = new JComboBox<>(estadosPosibles.toArray(new String[0]));
        comboEstados.setSelectedItem(estadoActual); // Preseleccionar el estado actual

        // Campo para observaciones
        JTextField txtObservaciones = new JTextField(20);

        panelDialogo.add(new JLabel("Seleccione el nuevo estado:"));
        panelDialogo.add(comboEstados);
        panelDialogo.add(Box.createVerticalStrut(15)); // Un espaciador
        panelDialogo.add(new JLabel("Observaciones (opcional):"));
        panelDialogo.add(txtObservaciones);

        int resultado = JOptionPane.showConfirmDialog(this, panelDialogo, "Actualizar Estado de Encomienda", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String nuevoEstado = (String) comboEstados.getSelectedItem();
            String observaciones = txtObservaciones.getText();

            // Llamamos al método del DAO para actualizar
            encomiendaDAO.actualizarEstado(encomiendaId, nuevoEstado, empleadoLogueado.getEmpleadoID(), observaciones);

            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarEncomiendasEnTabla(); // Refrescar la tabla
        }
    }

    // --- El resto de los métodos (cargarClientes, registrarEncomienda, etc.) se mantienen igual ---
    public void cargarClientesEnComboBox() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel<>();
        for (Cliente cliente : clientes) {
            model.addElement(cliente);
        }
        comboClientes.setModel(model);
    }
    private void cargarEncomiendasEnTabla() {
        String[] columnas = {"ID", "Remitente", "Destinatario", "Estado", "Fecha Ingreso"};
        DefaultTableModel tableModel = new DefaultTableModel(columnas, 0);

        List<Encomienda> encomiendas = encomiendaDAO.listarTodas();
        for (Encomienda enc : encomiendas) {
            Object[] fila = {
                    enc.getEncomiendaID(),
                    enc.getClienteRemitente().getNombres() + " " + enc.getClienteRemitente().getApellidos(),
                    enc.getNombreDestinatario(),
                    enc.getEstadoActual(),
                    enc.getFechaIngreso().toLocalDate()
            };
            tableModel.addRow(fila);
        }
        tablaEncomiendas.setModel(tableModel);
    }
    private void limpiarCampos() {
        comboClientes.setSelectedIndex(0);
        txtDestinatarioNombre.setText("");
        txtDestinatarioDireccion.setText("");
        txtDestinatarioTelefono.setText("");
        txtAreaDescripcion.setText("");
        txtPeso.setText("");
    }
    private void registrarEncomienda() {
        if (txtDestinatarioNombre.getText().isEmpty() || txtPeso.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del destinatario y el peso son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Encomienda nuevaEncomienda = new Encomienda();
        nuevaEncomienda.setClienteRemitente((Cliente) comboClientes.getSelectedItem());
        nuevaEncomienda.setEmpleadoRegistra(this.empleadoLogueado);
        nuevaEncomienda.setNombreDestinatario(txtDestinatarioNombre.getText());
        nuevaEncomienda.setDireccionDestino(txtDestinatarioDireccion.getText());
        nuevaEncomienda.setTelefonoDestino(txtDestinatarioTelefono.getText());
        nuevaEncomienda.setDescripcionPaquete(txtAreaDescripcion.getText());
        try {
            nuevaEncomienda.setPesoKG(Double.parseDouble(txtPeso.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El peso debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        encomiendaDAO.registrar(nuevaEncomienda);
        JOptionPane.showMessageDialog(this, "Encomienda registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
        cargarEncomiendasEnTabla();
    }

    private void verHistorial() {
        int filaSeleccionada = tablaEncomiendas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una encomienda de la tabla para ver su historial.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int encomiendaId = (int) tablaEncomiendas.getValueAt(filaSeleccionada, 0);

        // Obtenemos la ventana principal para usarla como "padre" del diálogo
        JFrame framePadre = (JFrame) SwingUtilities.getWindowAncestor(this);
        FormHistorial dialogo = new FormHistorial(framePadre, encomiendaId);
        dialogo.setVisible(true);
    }
}