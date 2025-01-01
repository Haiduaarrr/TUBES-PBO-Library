package com.example.library;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.sql.Connection;

public class DatabaseConnectionTest extends AppCompatActivity {

    private static final String TAG = "DatabaseConnectionTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_connection_test);

        Log.d(TAG, "Activity DatabaseConnectionTest dimulai.");

        // Test the database connection
        testDatabaseConnection();
    }

    private void testDatabaseConnection() {
        Connection connection = null;
        try {
            // Attempt to connect to the database
            connection = DatabaseConnection.connect();
            if (connection != null) {
                Log.d(TAG, "Koneksi berhasil.");
            } else {
                Log.e(TAG, "Koneksi gagal.");
            }
        } catch (Exception e) {
            // Log any exceptions during the connection process
            Log.e(TAG, "Error saat mencoba koneksi ke database: ", e);
        } finally {
            // Close the connection if it was successful
            if (connection != null) {
                try {
                    connection.close();
                    Log.d(TAG, "Koneksi ditutup.");
                } catch (Exception e) {
                    Log.e(TAG, "Error saat menutup koneksi: ", e);
                }
            }
        }
    }
}
