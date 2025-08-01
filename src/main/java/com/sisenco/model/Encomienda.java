package com.sisenco.model;

import java.time.LocalDateTime;

/**
 * Representa la entidad Encomienda, el objeto principal del dominio del negocio.
 * <p>
 * Esta clase es un POJO (Plain Old Java Object) que encapsula todos los datos
 * relacionados con un envío, desde su origen hasta su destino y estado actual.
 * Actúa como un DTO (Data Transfer Object) para mover esta información entre las
 * diferentes capas de la aplicación.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class Encomienda {

    // --- ATRIBUTOS ---
    /**
     * El identificador único de la encomienda en la base de datos (Clave Primaria).
     */
    private int encomiendaID;
    /**
     * El objeto {@link Cliente} que actúa como remitente de la encomienda.
     * Representa una relación (clave foránea a Clientes).
     */
    private Cliente clienteRemitente;
    /**
     * El objeto {@link Empleado} que registró la encomienda en el sistema.
     * Representa una relación (clave foránea a Empleados).
     */
    private Empleado empleadoRegistra;
    /**
     * El nombre completo de la persona que recibirá la encomienda.
     */
    private String nombreDestinatario;
    /**
     * La dirección completa a la que se debe entregar la encomienda.
     */
    private String direccionDestino;
    /**
     * El número de teléfono de contacto del destinatario.
     */
    private String telefonoDestino;
    /**
     * Una descripción del contenido del paquete.
     */
    private String descripcionPaquete;
    /**
     * El peso del paquete en kilogramos.
     */
    private double pesoKG;
    /**
     * El nombre del estado actual de la encomienda (ej. "Ingresado", "En Tránsito").
     */
    private String estadoActual;
    /**
     * La fecha y hora exactas en que la encomienda fue registrada en el sistema.
     */
    private LocalDateTime fechaIngreso;

    // --- CONSTRUCTORES ---

    /**
     * Constructor por defecto.
     * Permite la creación de una instancia vacía de Encomienda.
     */
    public Encomienda() {
    }

    // --- GETTERS Y SETTERS ---

    public int getEncomiendaID() {
        return encomiendaID;
    }

    public void setEncomiendaID(int encomiendaID) {
        this.encomiendaID = encomiendaID;
    }

    public Cliente getClienteRemitente() {
        return clienteRemitente;
    }

    public void setClienteRemitente(Cliente clienteRemitente) {
        this.clienteRemitente = clienteRemitente;
    }

    public Empleado getEmpleadoRegistra() {
        return empleadoRegistra;
    }

    public void setEmpleadoRegistra(Empleado empleadoRegistra) {
        this.empleadoRegistra = empleadoRegistra;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public String getTelefonoDestino() {
        return telefonoDestino;
    }

    public void setTelefonoDestino(String telefonoDestino) {
        this.telefonoDestino = telefonoDestino;
    }

    public String getDescripcionPaquete() {
        return descripcionPaquete;
    }

    public void setDescripcionPaquete(String descripcionPaquete) {
        this.descripcionPaquete = descripcionPaquete;
    }

    public double getPesoKG() {
        return pesoKG;
    }

    public void setPesoKG(double pesoKG) {
        this.pesoKG = pesoKG;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}