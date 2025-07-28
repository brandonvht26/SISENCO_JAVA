package com.sisenco.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.sisenco.dao.BodegaDAO;
import com.sisenco.dao.EmpleadoDAO;
import com.sisenco.dao.RolDAO;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Map;

public class FormUsuarios extends JPanel {
    // --- Componentes de la Interfaz (deben coincidir con los nombres de campo en el .form) ---
    private JPanel panelPrincipal;
    private JTextField txtUsuario;
    private JPasswordField passClave;
    private JComboBox<String> comboRol;
    private JTextField txtCedula;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JComboBox<String> comboBodega;
    private JButton btnCrear;

    // --- Clases de Acceso a Datos (DAO) ---
    private EmpleadoDAO empleadoDAO;
    private RolDAO rolDAO;
    private BodegaDAO bodegaDAO;

    // Mapa para almacenar la relación NombreBodega -> BodegaID
    private Map<String, Integer> bodegasMap;

    public FormUsuarios() {
        // Agrega el panel diseñado a esta clase
        add(panelPrincipal);

        // Inicializa los DAOs
        empleadoDAO = new EmpleadoDAO();
        rolDAO = new RolDAO();
        bodegaDAO = new BodegaDAO();

        // Carga los datos iniciales en los ComboBox
        cargarRoles();
        cargarBodegas();

        // Asigna el evento al botón de crear
        btnCrear.addActionListener(e -> crearUsuario());

        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre de usuario");
        passClave.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contraseña");
        txtCedula.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cédula");
        txtNombres.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombres");
        txtApellidos.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Apellidos");
        txtTelefono.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Teléfono");
        txtCorreo.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Correo");
        txtDireccion.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Dirección");
    }

    /**
     * Carga los nombres de los roles desde la BD al ComboBox de roles.
     */
    private void cargarRoles() {
        // Limpia el combo por si acaso
        comboRol.removeAllItems();
        // Pide la lista de roles al DAO y la agrega al combo
        rolDAO.listarNombresRoles().forEach(comboRol::addItem);
    }

    /**
     * Carga los nombres de las bodegas desde la BD al ComboBox de bodegas.
     */
    private void cargarBodegas() {
        // Limpia el combo
        comboBodega.removeAllItems();
        // Pide el mapa (Nombre -> ID) al DAO
        bodegasMap = bodegaDAO.listarBodegas();
        // Agrega solo los nombres (las claves del mapa) al combo
        bodegasMap.keySet().forEach(comboBodega::addItem);
    }

    /**
     * Recoge los datos del formulario y llama al DAO para crear el nuevo empleado y usuario.
     */
    private void crearUsuario() {
        // Validación simple de campos
        if (txtUsuario.getText().isBlank() || new String(passClave.getPassword()).isBlank() || txtCedula.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Usuario, Clave y Cédula son campos obligatorios.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Recoger datos de la UI
        String nombreUsuario = txtUsuario.getText();
        String clave = new String(passClave.getPassword());
        String rol = (String) comboRol.getSelectedItem();
        String cedula = txtCedula.getText();
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        String direccion = txtDireccion.getText();
        // Obtener el ID de la bodega seleccionada usando el mapa
        Integer bodegaId = bodegasMap.get((String) comboBodega.getSelectedItem());

        // Llamada al DAO dentro de un try-catch para manejar errores de la BD
        try {
            empleadoDAO.crearEmpleadoYUsuario(nombreUsuario, clave, rol, cedula, nombres, apellidos, telefono, correo, direccion, bodegaId);
            JOptionPane.showMessageDialog(this, "Usuario y empleado creados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } catch (SQLException ex) {
            // Mostramos el mensaje de error que viene de la base de datos (ej: cédula o usuario duplicado)
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarCampos() {
        txtUsuario.setText("");
        passClave.setText("");
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        if (comboRol.getItemCount() > 0) comboRol.setSelectedIndex(0);
        if (comboBodega.getItemCount() > 0) comboBodega.setSelectedIndex(0);
    }
}