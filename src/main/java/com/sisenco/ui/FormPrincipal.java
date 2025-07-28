package com.sisenco.ui;

import com.sisenco.model.Usuario;
import javax.swing.*;

public class FormPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPanePrincipal;
    private Usuario usuarioLogueado;

    public FormPrincipal(Usuario usuario) {
        super("SISENCO - Sistema de Encomiendas");
        this.usuarioLogueado = usuario; // Guardamos el usuario

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        // --- Pestañas ---
        tabbedPanePrincipal.addTab("Gestionar Clientes", new FormClientes());
        tabbedPanePrincipal.addTab("Gestionar Encomiendas", new FormEncomiendas(this.usuarioLogueado));

        // ---- LÓGICA DE SEGURIDAD POR ROL ----
        if ("ADMINISTRADOR".equals(usuario.getRol())) {
            tabbedPanePrincipal.addTab("Gestionar Usuarios", new FormUsuarios());
            // AÑADIMOS LA NUEVA PESTAÑA DE REPORTES AQUÍ
            tabbedPanePrincipal.addTab("Reportes", new FormReportes());
        }
    }
}