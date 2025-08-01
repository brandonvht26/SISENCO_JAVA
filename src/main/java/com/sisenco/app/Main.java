package com.sisenco.app;

import com.formdev.flatlaf.FlatLightLaf;
import com.sisenco.model.Usuario;
import com.sisenco.ui.FormLogin;
import com.sisenco.ui.FormPrincipal;

import javax.swing.*;

/**
 * Esta es la clase principal que sirve como punto de entrada para la aplicación SISENCO.
 * <p>
 * Su responsabilidad es:
 * 1) Configurar el entorno gráfico (Look and Feel)
 * 2) Gestionar el flujo de autenticación inicial y lanzar la ventana principal de la aplicación si el login es exitoso.
 * <p>
 * Todo el proceso de inicio de sesión y navegación en la aplicación se puede obersvar en el video: https://youtu.be/aaHpGQKzgvk.
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */

public class Main {

    /**
     * Método principal cuya ejecución inicia el corrido de la aplicación
     *
     * @param args Argumentos de línea de comandos (no se utilizan en esta aplicación).
     */
    public static void main(String[] args) {

        // --- MÓDULO DE LA CONFIGURACIÓN DE INTERFAZ DE USUARIO (UI) ---

        // Se establece el Look and Feel "FlatLaf" para darle un aspecto moderno y limpio a todos los componentes de
        // Swing GUI. Esto debe hacerse antes de crear cualquier ventana (JFrame, JDialog, etc.).

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            // En caso de que el Look and Feel no sea compatible, se imprime un error.
            // La aplicación continuará con el Look and Feel por defecto de Java.
            System.err.println("Falló la inicialización del Look and Feel FlatLaf.");
            e.printStackTrace();
        }

        // --- MÓDULO DE ARRANQUE DE LA APLICACIÓN ---
        // Se utiliza SwingUtilities.invokeLater para asegurar que toda la creación y manipulación de la UI se realice
        // en el Event Dispatch Thread (EDT). Esta es la práctica estándar y segura para evitar problemas de
        // concurrencia en Swing.
        SwingUtilities.invokeLater(() -> {
            // 1. PROCESO DE AUTENTICACIÓN
            // Se crea un diálogo modal, lo que significa que la ejecución del código no se detendrá hasta que el
            // usuario cierre la ventana de login.
            FormLogin formLogin = new FormLogin(null);
            formLogin.setVisible(true); // Muestra el diálogo y espera.

            // 2. VERIFICACIÓN DEL RESULTADO DEL LOGIN
            // Una vez que el diálogo de login se cierra, recuperamos el resultado.
            Usuario usuarioValidado = formLogin.getUsuarioValidado();

            // 3. FLUJO CONDICIONAL DE LA AUTENTICACIÓN
            if (usuarioValidado != null) {
                // Si el login fue exitoso, el objeto 'usuarioValidado' no será nulo, se crea y muestra la ventana
                // principal. Se le pasa el objeto 'usuarioValidado' para que la ventana principal conozca qué usuario
                // ha iniciado sesión y pueda gestionar los permisos.
                FormPrincipal formPrincipal = new FormPrincipal(usuarioValidado);
                formPrincipal.setVisible(true);
            } else {
                // Si el usuario cierra la ventana de login o las credenciales son incorrectas, 'usuarioValidado' será
                // null. En este caso, la aplicación simplemente termina; pero solo en el primer caso detendrá la
                // ejecución.
                System.out.println("Login cancelado o fallido. Saliendo de la aplicación.");
                System.exit(0);
            }
        });
    }
}