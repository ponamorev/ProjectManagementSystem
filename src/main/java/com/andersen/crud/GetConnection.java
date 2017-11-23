package com.andersen.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class GetConnection {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/task_sql" +
            "?autoReconnect=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "2130";

    static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }
}
