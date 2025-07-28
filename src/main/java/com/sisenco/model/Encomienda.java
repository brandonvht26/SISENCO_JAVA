package com.sisenco.model;

import java.time.LocalDateTime;

public class Encomienda {
    private int encomiendaID;
    private Cliente clienteRemitente;
    private Empleado empleadoRegistra;
    private String nombreDestinatario;
    private String direccionDestino;
    private String telefonoDestino;
    private String descripcionPaquete;
    private double pesoKG;
    private String estadoActual;
    private LocalDateTime fechaIngreso;

    // Constructor, Getters y Setters...
    // (Puedes pedírselos a Gemini en IntelliJ o generarlos con el IDE)

    public int getEncomiendaID() { return encomiendaID; }
    public void setEncomiendaID(int encomiendaID) { this.encomiendaID = encomiendaID; }
    public Cliente getClienteRemitente() { return clienteRemitente; }
    public void setClienteRemitente(Cliente clienteRemitente) { this.clienteRemitente = clienteRemitente; }
    public Empleado getEmpleadoRegistra() { return empleadoRegistra; }
    public void setEmpleadoRegistra(Empleado empleadoRegistra) { this.empleadoRegistra = empleadoRegistra; }
    public String getNombreDestinatario() { return nombreDestinatario; }
    public void setNombreDestinatario(String nombreDestinatario) { this.nombreDestinatario = nombreDestinatario; }
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
    public String getTelefonoDestino() { return telefonoDestino; }
    public void setTelefonoDestino(String telefonoDestino) { this.telefonoDestino = telefonoDestino; }
    public String getDescripcionPaquete() { return descripcionPaquete; }
    public void setDescripcionPaquete(String descripcionPaquete) { this.descripcionPaquete = descripcionPaquete; }
    public double getPesoKG() { return pesoKG; }
    public void setPesoKG(double pesoKG) { this.pesoKG = pesoKG; }
    public String getEstadoActual() { return estadoActual; }
    public void setEstadoActual(String estadoActual) { this.estadoActual = estadoActual; }
    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}