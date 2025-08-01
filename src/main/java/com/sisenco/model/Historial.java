package com.sisenco.model;

import java.time.LocalDateTime;

/**
 * Representa un único registro en el historial de estados de una encomienda.
 * <p>
 * Esta clase es un POJO (Plain Old Java Object) que actúa como un DTO (Data Transfer Object)
 * de solo lectura. Su propósito es encapsular la información de un cambio de estado
 * para ser mostrada en la interfaz de usuario, como en el diálogo "Historial de Encomienda".
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class Historial {

    // --- ATRIBUTOS ---
    /**
     * La fecha y hora exactas en que se registró el cambio de estado.
     */
    private LocalDateTime fechaCambio;
    /**
     * El nombre del estado asignado en este registro (ej. "En Tránsito").
     */
    private String nombreEstado;
    /**
     * El nombre completo del empleado que realizó el cambio de estado.
     */
    private String nombreEmpleado;
    /**
     * Observaciones o notas adicionales asociadas a este cambio de estado.
     */
    private String observaciones;

    // --- CONSTRUCTORES ---

    /**
     * Constructor por defecto.
     * Permite la creación de una instancia vacía de Historial.
     */
    public Historial() {
    }

    // --- GETTERS Y SETTERS ---

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}