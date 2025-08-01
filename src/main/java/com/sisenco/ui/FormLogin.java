package com.sisenco.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.sisenco.dao.UsuarioDAO;
import com.sisenco.model.Usuario;

import javax.swing.*;
import java.awt.*;

// CAMBIO 1: Ahora extiende de JDialog
public class FormLogin extends JDialog {
    private JPanel panelLogin;
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
    private JLabel labelLogo;
    private JLabel labelPortada;

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioValidado; // Para guardar el usuario que se logueó

    // CAMBIO 2: El constructor ahora recibe el 'padre' (la ventana principal)
    public FormLogin(Frame parent) {
        super(parent, "Inicio de Sesión", true);

        setContentPane(panelLogin);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(1000, 585);
        setLocationRelativeTo(parent);
        cargarImagenes();

        // 2. AÑADE LOS PLACEHOLDERS AQUÍ
        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Usuario");
        txtClave.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contraseña");

        usuarioDAO = new UsuarioDAO();
        btnIngresar.addActionListener(e -> realizarLogin());
    }

    private void realizarLogin() {
        String nombreUsuario = txtUsuario.getText();
        String clave = new String(txtClave.getPassword());

        if (nombreUsuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        usuarioValidado = usuarioDAO.validarUsuario(nombreUsuario, clave);

        if (usuarioValidado != null) {
            // Si el login es correcto, simplemente cerramos el diálogo
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CAMBIO 3: Un método público para que el exterior sepa si el login fue exitoso
    public Usuario getUsuarioValidado() {
        return this.usuarioValidado;
    }

    private void cargarImagenes() {
        // Busca la imagen del logo dentro del JAR (o classpath)
        java.net.URL logoUrl = getClass().getResource("/images/logo_SISENCO_sinFondo.png");
        if (logoUrl != null) {
            // Si la encuentra, la asigna al JLabel correspondiente.
            // Necesitarás vincular 'labelLogo' a la JLabel en el diseñador de UI.
            labelLogo.setIcon(new ImageIcon(logoUrl));
        } else {
            System.err.println("No se pudo encontrar el recurso: /images/logo_SISENCO_sinFondo.png");
        }

        // Lo mismo para la imagen de portada
        java.net.URL portadaUrl = getClass().getResource("/images/portada_login.jpg");
        if (portadaUrl != null) {
            // Necesitarás vincular 'labelPortada' a la JLabel en el diseñador de UI.
            labelPortada.setIcon(new ImageIcon(portadaUrl));
        } else {
            System.err.println("No se pudo encontrar el recurso: /images/portada_login.jpg");
        }
    }
}