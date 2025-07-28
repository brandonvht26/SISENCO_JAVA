package com.sisenco.ui;

import com.sisenco.model.Usuario;
import javax.swing.*;

public class FormPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JTabbedPane tabbedPanePrincipal;
    private Usuario usuarioLogueado;

    // --- PASO 1: Declarar una variable para guardar el formulario de encomiendas ---
    private FormEncomiendas panelEncomiendas;

    public FormPrincipal(Usuario usuario) {
        super("SISENCO - Sistema de Encomiendas");
        this.usuarioLogueado = usuario; // Guardamos el usuario

        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 910); // Ajusté un poco el tamaño para que quepa mejor
        setLocationRelativeTo(null);

        // --- PASO 2: Crear la instancia y guardarla en nuestra variable ---
        this.panelEncomiendas = new FormEncomiendas(this.usuarioLogueado);

        // --- Pestañas ---
        tabbedPanePrincipal.addTab("Gestionar Clientes", new FormClientes());
        // --- PASO 3: Usar la instancia que ya creamos ---
        tabbedPanePrincipal.addTab("Gestionar Encomiendas", this.panelEncomiendas);

        // ---- LÓGICA DE SEGURIDAD POR ROL ----
        if ("ADMINISTRADOR".equals(usuario.getRol())) {
            tabbedPanePrincipal.addTab("Gestionar Usuarios", new FormUsuarios());
            // AÑADIMOS LA NUEVA PESTAÑA DE REPORTES AQUÍ
            tabbedPanePrincipal.addTab("Reportes", new FormReportes());
        }

        tabbedPanePrincipal.addChangeListener(e -> {
            // Verificamos si la pestaña seleccionada es la de "Gestionar Encomiendas"
            if (tabbedPanePrincipal.getSelectedIndex() != -1 &&
                    tabbedPanePrincipal.getSelectedComponent() == this.panelEncomiendas) { // Comprobación más segura

                // --- PASO 4: Llamar al método usando la referencia correcta ---
                this.panelEncomiendas.cargarClientesEnComboBox();
            }
        });
    }
}