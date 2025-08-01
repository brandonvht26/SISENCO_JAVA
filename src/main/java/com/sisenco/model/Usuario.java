package com.sisenco.model;

/**
 * Representa la entidad Usuario en el sistema, enfocada en la autenticación y autorización.
 * <p>
 * Esta clase es un POJO (Plain Old Java Object) que actúa como un DTO (Data Transfer Object)
 * para mantener los datos de la sesión del usuario que ha iniciado sesión. Contiene la información
 * esencial para gestionar permisos y personalizar la interfaz de usuario.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.1 - Añadido constructor parametrizado.
 */
public class Usuario {

    // --- ATRIBUTOS ---
    /**
     * El identificador único del usuario en la base de datos (Clave Primaria).
     */
    private int usuarioID;
    /**
     * El nombre de usuario utilizado para el login.
     */
    private String nombreUsuario;
    /**
     * El nombre del rol asignado al usuario (ej. "ADMINISTRADOR", "OPERADOR").
     * Este atributo es crucial para controlar el acceso a las diferentes funcionalidades
     * de la aplicación.
     */
    private String rol;
    /**
     * Indica si la cuenta de usuario está activa (true) o inactiva (false).
     * Solo los usuarios activos pueden iniciar sesión.
     */
    private boolean activo;

    // --- CONSTRUCTORES ---

    /**
     * Constructor por defecto.
     * Permite la creación de una instancia vacía de Usuario.
     */
    public Usuario() {
    }

    /**
     * Constructor parametrizado para crear una instancia de Usuario con todos sus datos.
     *
     * @param usuarioID     El ID del usuario.
     * @param nombreUsuario El nombre de usuario.
     * @param rol           El rol del usuario.
     * @param activo        El estado de activación del usuario.
     */
    public Usuario(int usuarioID, String nombreUsuario, String rol, boolean activo) {
        this.usuarioID = usuarioID;
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
        this.activo = activo;
    }


    // --- GETTERS Y SETTERS ---

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}