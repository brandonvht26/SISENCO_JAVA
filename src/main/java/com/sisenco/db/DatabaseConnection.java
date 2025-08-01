package com.sisenco.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad (utility class) para gestionar y centralizar la creación de conexiones a la base de datos.
 * <p>
 * Actúa como una fábrica que proporciona objetos {@link Connection} configurados para conectarse
 * a la base de datos SISENCO alojada en Azure SQL.
 *
 * @author Brandon Huera Torres (GitHub: brandonvht26)
 * @version 1.0
 */
public class DatabaseConnection {

    // --- MÓDULO DE CONFIGURACIÓN DE LA CONEXIÓN ---

    // Credenciales y detalles del servidor de base de datos en Azure.
    private static final String SERVER_NAME = "sisenco-server-epn-brandon.database.windows.net";
    private static final String DATABASE_NAME = "SISENCO";

    // NOTA DE SEGURIDAD IMPORTANTE: En una aplicación de producción, las credenciales NUNCA deben estar
    // escritas directamente en el código ("hardcodeadas"). Esto es un riesgo de seguridad grave.
    // La práctica recomendada es cargarlas desde variables de entorno, un archivo de configuración
    // externo (fuera del control de versiones) o un servicio de gestión de secretos como Azure Key Vault.
    private static final String USER = "admin_sisenco";
    private static final String PASSWORD = "Brandon.2.0";

    // Se construye la URL de conexión JDBC para SQL Server en Azure.
    // Los parámetros son específicos para una conexión segura a la nube:
    // - encrypt=true: Exige que toda la comunicación con la base de datos esté encriptada.
    // - trustServerCertificate=false: Obliga al driver a validar que el certificado del servidor es de confianza.
    // - hostNameInCertificate=*.database.windows.net: Verifica que el certificado fue emitido para el dominio de Azure.
    private static final String URL = String.format(
            "jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            SERVER_NAME, DATABASE_NAME
    );

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     * El diseño correcto es que nadie pueda crear un objeto `new DatabaseConnection()`,
     * forzando el uso del método estático `getConnection()`.
     */
    private DatabaseConnection() {}

    /**
     * Establece y devuelve una nueva conexión a la base de datos.
     * <p>
     * Cada vez que se llama a este método, se intenta crear una nueva sesión con la base de datos.
     * Es responsabilidad del código que llama a este método cerrar la conexión adecuadamente,
     * preferiblemente utilizando un bloque try-with-resources para garantizar que no haya fugas de recursos.
     *
     * @return Un objeto {@link Connection} que representa la sesión con la base de datos.
     * @throws SQLException si no se puede encontrar el driver de JDBC o si falla el intento de conexión
     *                      (ej. credenciales incorrectas, problemas de red, servidor no disponible).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Aunque con los drivers JDBC 4.0+ esto no suele ser necesario (se registran automáticamente),
            // incluirlo explícitamente garantiza la compatibilidad y previene errores en ciertos entornos de ejecución.
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            // Se envuelve la ClassNotFoundException en una SQLException para mantener una única
            // firma de excepción en el método (throws SQLException) y proporcionar la causa raíz del fallo.
            throw new SQLException("No se encontró el driver de SQL Server", e);
        }
        // Se intenta establecer la conexión usando la URL y las credenciales definidas.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}