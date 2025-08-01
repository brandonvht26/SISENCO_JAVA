package com.sisenco.model;

/**
 * Representa la entidad Cliente en el sistema.
 * <p>
 * Esta clase es un POJO (Plain Old Java Object) que actúa como un DTO (Data Transfer Object)
 * para transportar los datos de un cliente entre la capa de acceso a datos (DAO) y la
 * interfaz de usuario (UI).
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class Cliente {

    // --- ATRIBUTOS ---
    /**
     * El identificador único del cliente en la base de datos (Clave Primaria).
     */
    private int clienteID;
    /**
     * El número de cédula de identidad del cliente.
     */
    private String cedula;
    /**
     * Los nombres del cliente.
     */
    private String nombres;
    /**
     * Los apellidos del cliente.
     */
    private String apellidos;
    /**
     * El número de teléfono de contacto del cliente.
     */
    private String telefono;
    /**
     * La dirección de residencia del cliente.
     */
    private String direccion;

    // --- CONSTRUCTORES ---

    /**
     * Constructor por defecto.
     * Permite la creación de una instancia vacía de Cliente.
     */
    public Cliente() {
    }

    /**
     * Constructor parametrizado para crear una instancia de Cliente con todos sus datos.
     *
     * @param clienteID   El ID del cliente.
     * @param cedula      La cédula del cliente.
     * @param nombres     Los nombres del cliente.
     * @param apellidos   Los apellidos del cliente.
     * @param telefono    El teléfono del cliente.
     * @param direccion   La dirección del cliente.
     */
    public Cliente(int clienteID, String cedula, String nombres, String apellidos, String telefono, String direccion) {
        this.clienteID = clienteID;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // --- GETTERS Y SETTERS ---

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

    /**
     * Sobrescribe el método toString para proporcionar una representación textual significativa del objeto Cliente.
     * <p>
     * Este formato es utilizado automáticamente por componentes de Swing como {@link JComboBox} o {@link JList}
     * para mostrar el objeto de una manera legible para el usuario, evitando la necesidad de crear un
     * {@code ListCellRenderer} personalizado.
     *
     * @return Una cadena con el formato "Nombres Apellidos (Cédula)".
     */
    @Override
    public String toString() {
        // Esto hará que en el ComboBox se muestre "Nombres Apellidos (Cédula)"
        return nombres + " " + apellidos + " (" + cedula + ")";
    }
}