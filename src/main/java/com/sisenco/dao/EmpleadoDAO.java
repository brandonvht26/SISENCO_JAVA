package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO (Data Access Object) para gestionar las operaciones relacionadas con la entidad {@link Empleado}.
 * <p>
 * Esta clase se encarga de la interacción con la tabla {@code Empleados} y de operaciones complejas
 * que involucran tanto a empleados como a usuarios, como la creación conjunta a través de procedimientos almacenados.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class EmpleadoDAO {

    /**
     * Busca y recupera los datos básicos de un empleado basándose en su ID de usuario asociado.
     * <p>
     * Este método es útil para obtener rápidamente el nombre y apellido de un empleado que ha iniciado sesión,
     * por ejemplo, para mostrar un mensaje de bienvenida en la interfaz.
     *
     * @param usuarioId El ID del usuario ({@code UsuarioID}) cuyo perfil de empleado se desea encontrar.
     * @return Un objeto {@link Empleado} con el ID, nombres y apellidos si se encuentra.
     *         Retorna {@code null} si no hay ningún empleado asociado a ese ID de usuario.
     */
    public Empleado buscarPorUsuarioId(int usuarioId) {
        // --- MÓDULO DE INICIALIZACIÓN ---
        Empleado empleado = null;
        // Consulta SQL para obtener datos específicos del empleado a partir del UsuarioID.
        String sql = "SELECT EmpleadoID, Nombres, Apellidos FROM Empleados WHERE UsuarioID = ?";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza try-with-resources para la gestión automática de recursos.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se establece el parámetro UsuarioID en la consulta.
            pstmt.setInt(1, usuarioId);

            // Se ejecuta la consulta.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Si se encuentra un registro, se crea el objeto Empleado.
                if (rs.next()) {
                    empleado = new Empleado(
                            rs.getInt("EmpleadoID"),
                            rs.getString("Nombres"),
                            rs.getString("Apellidos")
                    );
                }
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            System.err.println("Error al buscar empleado por ID de usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return empleado;
    }

    /**
     * Crea un nuevo usuario y su perfil de empleado correspondiente en una única operación transaccional
     * mediante la ejecución de un procedimiento almacenado.
     * <p>
     * Este método es invocado desde la pestaña "Gestionar Usuarios" al registrar un nuevo empleado.
     * El uso de un procedimiento almacenado ({@code sp_CrearUsuarioEmpleado}) garantiza la atomicidad:
     * o se crean ambos registros (usuario y empleado) correctamente, o no se crea ninguno,
     * evitando inconsistencias en los datos.
     *
     * @param nombreUsuario El nombre de usuario para el login.
     * @param clave La contraseña del nuevo usuario.
     * @param nombreRol El rol asignado al usuario (ej. "OPERADOR").
     * @param cedula La cédula del empleado.
     * @param nombres Los nombres del empleado.
     * @param apellidos Los apellidos del empleado.
     * @param telefono El teléfono de contacto del empleado.
     * @param correo El correo electrónico del empleado.
     * @param direccion La dirección del empleado.
     * @param bodegaId El ID de la bodega a la que estará asignado el empleado.
     * @throws SQLException Si ocurre un error durante la ejecución del procedimiento almacenado.
     *                      La excepción se relanza para que la capa de UI pueda capturarla y notificar
     *                      al usuario (por ejemplo, si el nombre de usuario ya existe).
     */
    public void crearEmpleadoYUsuario(String nombreUsuario, String clave, String nombreRol, String cedula,
                                      String nombres, String apellidos, String telefono, String correo,
                                      String direccion, int bodegaId) throws SQLException {

        // --- MÓDULO DE EJECUCIÓN DE PROCEDIMIENTO ALMACENADO ---
        // Se define la llamada al procedimiento almacenado con sus 10 parámetros.
        String sql = "EXEC sp_CrearUsuarioEmpleado ?, ?, ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se asignan todos los parámetros en el orden correcto.
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, clave);
            pstmt.setString(3, nombreRol);
            pstmt.setString(4, cedula);
            pstmt.setString(5, nombres);
            pstmt.setString(6, apellidos);
            pstmt.setString(7, telefono);
            pstmt.setString(8, correo);
            pstmt.setString(9, direccion);
            pstmt.setInt(10, bodegaId);

            // Se ejecuta el procedimiento almacenado. No devuelve un ResultSet.
            pstmt.execute();

        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            // Se relanza la excepción para que la capa superior (UI) pueda manejarla.
            // Esto es crucial para informar al usuario sobre errores específicos, como un nombre de usuario duplicado.
            throw new SQLException("Error al crear el usuario y empleado: " + e.getMessage(), e);
        }
    }

}