package com.sisenco.app;

import com.formdev.flatlaf.FlatLightLaf; //  <-- 1. IMPORTA LA LIBRERÍA
import com.sisenco.model.Usuario;
import com.sisenco.ui.FormLogin;
import com.sisenco.ui.FormPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // 2. CONFIGURA EL LOOK AND FEEL ANTES DE TODO
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize LaF");
            // Manejar el error, aunque es poco común que falle.
        }

        SwingUtilities.invokeLater(() -> {
            // 1. Crear y mostrar el diálogo de login.
            //    Pasamos 'null' como padre para que sea modal a toda la aplicación.
            FormLogin formLogin = new FormLogin(null);
            formLogin.setVisible(true);

            // 2. El código se detiene aquí hasta que el diálogo de login se cierre.
            //    Después de que se cierra, obtenemos el resultado.
            Usuario usuarioValidado = formLogin.getUsuarioValidado();

            // 3. Si el login fue exitoso (el usuario no es null), CREAMOS y mostramos la ventana principal.
            if (usuarioValidado != null) {
                // Creamos la ventana principal AQUÍ, pasándole el usuario que acabamos de validar.
                FormPrincipal formPrincipal = new FormPrincipal(usuarioValidado);
                formPrincipal.setVisible(true);
            } else {
                // 4. Si el login no fue exitoso (el usuario cerró el diálogo), la aplicación termina.
                System.out.println("Login cancelado o fallido. Saliendo de la aplicación.");
                System.exit(0);
            }
        });
    }
}