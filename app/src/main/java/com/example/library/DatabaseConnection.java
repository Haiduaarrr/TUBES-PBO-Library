package com.example.library;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/perpustakaan";
    private static final String USER = "root"; // Ganti dengan username database Anda
    private static final String PASSWORD = "!#G43(m*Sd$x!"; // Ganti dengan password database Anda

    public static Connection connect() {
        Connection connection = null;
        try {
            // Mengizinkan operasi jaringan di thread utama (hanya untuk pengujian)
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Memuat driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Membuka koneksi ke database
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.d("DatabaseConnection", "Connection successful");
        } catch (Exception e) {
            Log.e("DatabaseConnection", "Connection error: " + e.getMessage());
        }
        return connection;
    }
}
