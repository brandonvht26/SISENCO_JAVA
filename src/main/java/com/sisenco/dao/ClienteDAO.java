package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para las operaciones CRUD (Crear, Leer, Actualizar, Eliminar) de la entidad {@link Cliente}.
 * <p>
 * Esta clase centraliza toda la interacción con la tabla {@code Clientes} de la base de datos. Es utilizada
 * intensivamente por la pestaña "Gestionar Clientes" para realizar todas sus funciones.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class ClienteDAO {

    /**
     * Busca un cliente específico en la base de datos utilizando su número de cédula.
     * <p>
     * Este método es fundamental para la funcionalidad de búsqueda en la pestaña de gestión de clientes.
     *
     * @param cedula El número de cédula ({@code String}) del cliente que se desea encontrar. @return Un objeto
     * {@link Cliente} con los datos del cliente si se encuentra. Retorna {@code null} si no existe ningún cliente con
     * esa cédula.
     */
    public Cliente buscarPorCedula(String cedula) {
        // --- MÓDULO DE INICIALIZACIÓN ---
        Cliente cliente = null;
        // Consulta SQL parametrizada para evitar inyección SQL.
        // "?" Representa un parámetro que será reemplazado por un valor en ejecución.
        String sql = "SELECT * FROM Clientes WHERE Cedula = ?";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza try-with-resources para garantizar el cierre automático de la conexión y el PreparedStatement.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Se asigna el valor del parámetro de la cédula a la consulta.
            pstmt.setString(1, cedula);

            // Se ejecuta la consulta y se obtiene el resultado.
            try (ResultSet rs = pstmt.executeQuery()) {
                // Si el ResultSet tiene al menos una fila, significa que se encontró el cliente.
                if (rs.next()) {
                    // Se crea un nuevo objeto Cliente y se puebla con los datos de la base de datos.
                    cliente = new Cliente(
                            rs.getInt("ClienteID"),
                            rs.getString("Cedula"),
                            rs.getString("Nombres"),
                            rs.getString("Apellidos"),
                            rs.getString("Telefono"),
                            rs.getString("Direccion")
                    );
                }
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            System.err.println("Error al buscar cliente por cédula: " + e.getMessage());
            e.printStackTrace();
        }
        return cliente;
    }

    /**
     * Guarda o actualiza un cliente en la base de datos.
     * <p>
     * Implementa una lógica "upsert":
     * <ul>
     *     <li>Si el {@code cliente.getClienteID()} es 0, se asume que es un cliente nuevo y se realiza un {@code INSERT}.</li>
     *     <li>Si el {@code cliente.getClienteID()} es mayor que 0, se asume que es un cliente existente y se realiza un {@code UPDATE}.</li>
     * </ul>
     *
     * @param cliente El objeto {@link Cliente} que contiene los datos a guardar o actualizar.
     */
    public void guardar(Cliente cliente) {
        // Se determina si la operación es una inserción o una actualización basándose en el ID del cliente.
        if (cliente.getClienteID() == 0) {
            // --- LÓGICA DE INSERCIÓN (INSERT) ---
            String sql = "INSERT INTO Clientes (Cedula, Nombres, Apellidos, Telefono, Direccion) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 // Se especifica Statement.RETURN_GENERATED_KEYS para poder recuperar el ID autoincremental.
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, cliente.getCedula());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());

                int affectedRows = pstmt.executeUpdate();

                // Si la inserción fue exitosa, se recupera el ID generado por la BD.
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            // Se actualiza el objeto Cliente con el nuevo ID. Esto es crucial para mantener
                            // la consistencia entre el objeto en memoria y el registro en la base de datos.
                            cliente.setClienteID(generatedKeys.getInt(1));
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al insertar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // --- LÓGICA DE ACTUALIZACIÓN (UPDATE) ---
            String sql = "UPDATE Clientes SET Cedula = ?, Nombres = ?, Apellidos = ?, Telefono = ?, Direccion = ? WHERE ClienteID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, cliente.getCedula());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());
                pstmt.setInt(6, cliente.getClienteID()); // El ID se usa en la cláusula WHERE.

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al actualizar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina un cliente de la base de datos utilizando su ID.
     *
     * @param clienteID El ID del cliente que se va a eliminar.
     */
    public void eliminar(int clienteID) {
        String sql = "DELETE FROM Clientes WHERE ClienteID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Nota: Un error común aquí es una SQLException por violación de clave foránea
            // si el cliente tiene encomiendas asociadas. La UI debería manejar esto.
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recupera una lista de todos los clientes registrados en la base de datos.
     * <p>
     * Este método es esencial para dos funcionalidades clave:
     * 1. Poblar la tabla de clientes en la pestaña "Gestionar Clientes".
     * 2. Llenar el ComboBox de remitentes en la pestaña "Gestionar Encomiendas".
     *
     * @return Una {@link List} de objetos {@link Cliente}. Si no hay clientes o hay un error,
     *         devuelve una lista vacía.
     */
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY Apellidos, Nombres"; // Ordenar alfabéticamente
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Se itera sobre cada registro del resultado.
            while (rs.next()) {
                // Por cada registro, se crea un objeto Cliente y se añade a la lista.
                clientes.add(new Cliente(
                        rs.getInt("ClienteID"),
                        rs.getString("Cedula"),
                        rs.getString("Nombres"),
                        rs.getString("Apellidos"),
                        rs.getString("Telefono"),
                        rs.getString("Direccion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return clientes;
    }
}