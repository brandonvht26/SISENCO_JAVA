package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import com.sisenco.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    /**
     * Busca un cliente en la base de datos por su número de cédula.
     * @param cedula La cédula del cliente a buscar.
     * @return un objeto Cliente si se encuentra, o null si no existe.
     */
    public Cliente buscarPorCedula(String cedula) {
        // ... (Este método ya lo tienes y funciona, lo dejamos como está)
        Cliente cliente = null;
        String sql = "SELECT * FROM Clientes WHERE Cedula = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
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
            System.err.println("Error al buscar cliente por cédula: " + e.getMessage());
            e.printStackTrace();
        }
        return cliente;
    }

    /**
     * Guarda un cliente en la base de datos. Si el cliente es nuevo (ID=0), lo inserta.
     * Si el cliente ya existe (ID > 0), lo actualiza.
     * @param cliente El objeto Cliente a guardar.
     */
    public void guardar(Cliente cliente) {
        // Si el ID es 0, es un cliente nuevo (INSERT). De lo contrario, es una actualización (UPDATE).
        if (cliente.getClienteID() == 0) {
            String sql = "INSERT INTO Clientes (Cedula, Nombres, Apellidos, Telefono, Direccion) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, cliente.getCedula());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Obtener el ID generado por la base de datos y asignarlo al objeto
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            cliente.setClienteID(generatedKeys.getInt(1));
                        }
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error al insertar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            String sql = "UPDATE Clientes SET Cedula = ?, Nombres = ?, Apellidos = ?, Telefono = ?, Direccion = ? WHERE ClienteID = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, cliente.getCedula());
                pstmt.setString(2, cliente.getNombres());
                pstmt.setString(3, cliente.getApellidos());
                pstmt.setString(4, cliente.getTelefono());
                pstmt.setString(5, cliente.getDireccion());
                pstmt.setInt(6, cliente.getClienteID());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error al actualizar cliente: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Elimina un cliente de la base de datos por su ID.
     * @param clienteID El ID del cliente a eliminar.
     */
    public void eliminar(int clienteID) {
        String sql = "DELETE FROM Clientes WHERE ClienteID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Devuelve una lista con todos los clientes de la base de datos.
     * @return una lista de objetos Cliente.
     */
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
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