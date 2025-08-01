package com.sisenco.dao;

import com.sisenco.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO (Data Access Object) para gestionar las operaciones de la tabla {@code Bodegas} en la base de datos.
 * <p>
 * Esta clase encapsula toda la lógica de acceso a los datos de las bodegas, permitiendo que el resto de la aplicación
 * interactúe con la información de las bodegas de una manera sencilla y desacoplada de la lógica SQL.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class BodegaDAO {

    /**
     * Recupera una lista de todas las bodegas disponibles en la base de datos.
     * <p>
     * Este método es utilizado, por ejemplo, en el formulario de reportes para poblar el ComboBox que permite filtrar
     * las bodegas, puesto a que estos están definidos a una tabla de la base de datos, aunque actualmente sólo habrá
     * una única bodega.
     *
     * @return Un {@link Map} donde la clave es el nombre de la bodega ({@code String}) y el valor es su
     *         ID correspondiente ({@code Integer}). Se utiliza un Map para asociar fácilmente el nombre
     *         visible en la UI con el ID necesario para las consultas a la base de datos.
     *         Retorna un mapa vacío si ocurre un error o no hay bodegas.
     */

    public Map<String, Integer> listarBodegas() {
        // --- MÓDULO DE INICIALIZACIÓN ---
        // Se crea un HashMap para almacenar los resultados. La clave será el nombre y el valor el ID.
        Map<String, Integer> bodegas = new HashMap<>();
        // Se define la consulta SQL para obtener el ID y el nombre de todas las bodegas.
        String sql = "SELECT BodegaID, NombreBodega FROM Bodegas";

        // --- MÓDULO DE CONEXIÓN Y EJECUCIÓN ---
        // Se utiliza un bloque try-with-resources para asegurar que la conexión (Connection), la declaración (Statement)
        // y el conjunto de resultados (ResultSet) se cierren automáticamente al finalizar, evitando fugas de recursos.
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Se itera sobre cada fila devuelta por la consulta.
            while (rs.next()) {
                // Por cada bodega encontrada, se añade una entrada al mapa.
                // Key: Nombre de la bodega (ej: "Bodega Quito").
                // Value: ID de la bodega (ej: 1).
                bodegas.put(rs.getString("NombreBodega"), rs.getInt("BodegaID"));
            }
        } catch (SQLException e) {
            // --- MÓDULO DE MANEJO DE ERRORES ---
            // Si ocurre cualquier error durante la conexión o la consulta SQL,
            // se imprime un mensaje de error en la consola para diagnóstico.
            System.err.println("Error al listar las bodegas desde la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        // Se devuelve el mapa con las bodegas (o un mapa vacío si hubo un error).
        return bodegas;
    }
}