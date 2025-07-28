package com.sisenco.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String SERVER_NAME = "sisenco-server-epn-brandon.database.windows.net";
    private static final String DATABASE_NAME = "SISENCO";
    private static final String URL = String.format(
            "jdbc:sqlserver://%s:1433;databaseName=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            SERVER_NAME, DATABASE_NAME
    );

    private static final String USER = "admin_sisenco";
    private static final String PASSWORD = "Brandon.2.0";

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver de SQL Server", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}