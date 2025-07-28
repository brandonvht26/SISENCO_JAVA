package com.sisenco.ui;

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

    private UsuarioDAO usuarioDAO;
    private Usuario usuarioValidado; // Para guardar el usuario que se logueó

    // CAMBIO 2: El constructor ahora recibe el 'padre' (la ventana principal)
    public FormLogin(Frame parent) {
        super(parent, "SISENCO - Inicio de Sesión", true); // true = es modal

        setContentPane(panelLogin);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Cierra solo el diálogo
        setSize(400, 250);
        setLocationRelativeTo(parent); // Se centra sobre el padre

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
}