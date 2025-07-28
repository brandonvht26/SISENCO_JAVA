package com.sisenco.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=SISENCO;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "Brandon.2.0";

    // Constructor privado para que no se pueda instanciar (clase de utilidad)
    private DatabaseConnection() {}

    /**
     * Crea y devuelve una nueva conexión a la base de datos.
     * @return una nueva Connection.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver es opcional en JDBC 4.0+ pero no hace daño
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // Envolvemos en SQLException para un manejo de errores consistente
            throw new SQLException("No se encontró el driver de SQL Server", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}