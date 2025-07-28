package com.sisenco.model;

public class Cliente {

    private int clienteID;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String direccion;

    // Constructor, getters y setters...

    public Cliente() {
    }

    public Cliente(int clienteID, String cedula, String nombres, String apellidos, String telefono, String direccion) {
        this.clienteID = clienteID;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        // Esto hará que en el ComboBox se muestre "Nombres Apellidos (Cédula)"
        return nombres + " " + apellidos + " (" + cedula + ")";
    }
}