package com.example.library;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String TAG = "DatabaseConnection";

    // Replace these values with your MySQL database details
    private static final String URL = "jdbc:mysql://10.0.2.2:3306/perpustakaan"; // For emulator use 10.0.2.2
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "!#G43(m*Sd$x!"; // Replace with your MySQL password

    public static Connection connect() {
        Connection connection = null;
        try {
            // Allow network operations on the main thread (for testing purposes only)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.d(TAG, "Connection successful!");
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Driver not found: ", e);
        } catch (SQLException e) {
            Log.e(TAG, "SQL connection error: ", e);
        }
        return connection;
    }
}
