package com.sisenco.model;

// Simplificaremos el modelo para lo que necesitamos ahora
public class Empleado {
    private int empleadoID;
    private String nombres;
    private String apellidos;

    public Empleado(int empleadoID, String nombres, String apellidos) {
        this.empleadoID = empleadoID;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getEmpleadoID() { return empleadoID; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }

    @Override
    public String toString() {
        return nombres + " " + apellidos;
    }
}