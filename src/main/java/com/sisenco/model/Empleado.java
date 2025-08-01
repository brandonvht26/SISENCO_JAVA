package com.sisenco.model;

/**
 * Representa la entidad Empleado en el sistema.
 * <p>
 * Esta clase es un POJO (Plain Old Java Object) que encapsula los datos de un empleado,
 * facilitando su transporte entre la capa de datos (DAO) y la interfaz de usuario (UI).
 * Se ha completado el modelo para incluir todos los campos relevantes de un empleado,
 * no solo los básicos, para dar soporte a operaciones como la creación de nuevos empleados.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.1 - Modelo completado con todos los campos.
 */
public class Empleado {

    // --- ATRIBUTOS ---
    /**
     * El identificador único del empleado en la base de datos (Clave Primaria).
     */
    private int empleadoID;
    /**
     * El número de cédula de identidad del empleado.
     */
    private String cedula;
    /**
     * Los nombres del empleado.
     */
    private String nombres;
    /**
     * Los apellidos del empleado.
     */
    private String apellidos;
    /**
     * El número de teléfono de contacto del empleado.
     */
    private String telefono;
    /**
     * El correo electrónico del empleado.
     */
    private String correo;
    /**
     * La dirección de residencia del empleado.
     */
    private String direccion;
    /**
     * El ID del usuario asociado a este empleado para el inicio de sesión (Clave Foránea a Usuarios).
     */
    private int usuarioID;
    /**
     * El ID de la bodega a la que está asignado el empleado (Clave Foránea a Bodegas).
     */
    private int bodegaID;


    // --- CONSTRUCTORES ---

    /**
     * Constructor por defecto.
     * Permite la creación de una instancia vacía de Empleado.
     */
    public Empleado() {
    }

    /**
     * Constructor simplificado para operaciones donde solo se necesita la identidad básica del empleado.
     * Utilizado, por ejemplo, al recuperar el empleado que ha iniciado sesión desde {@link com.sisenco.dao.EmpleadoDAO#buscarPorUsuarioId(int)}.
     *
     * @param empleadoID El ID del empleado.
     * @param nombres    Los nombres del empleado.
     * @param apellidos  Los apellidos del empleado.
     */
    public Empleado(int empleadoID, String nombres, String apellidos) {
        this.empleadoID = empleadoID;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    // --- GETTERS Y SETTERS ---

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getBodegaID() {
        return bodegaID;
    }

    public void setBodegaID(int bodegaID) {
        this.bodegaID = bodegaID;
    }

    /**
     * Sobrescribe el método toString para proporcionar una representación textual del nombre completo del empleado.
     *
     * @return Una cadena con el formato "Nombres Apellidos".
     */
    @Override
    public String toString() {
        return nombres + " " + apellidos;
    }
}