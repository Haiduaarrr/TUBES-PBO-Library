package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Akses elemen dari layout
        EditText username = findViewById(R.id.etUsername);
        EditText password = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister); // Tombol untuk register

        // Tambahkan logika tombol Login
        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            try (Connection connection = DatabaseConnection.connect()) {
                // Query untuk memeriksa kredensial dari database
                String query = "SELECT u.user_id, u.password, 'user' AS role FROM user u WHERE u.username = ? " +
                        "UNION " +
                        "SELECT a.admin_id, null AS password, 'admin' AS role FROM admin a " +
                        "JOIN person p ON a.person_id = p.person_id " +
                        "WHERE p.nama = ?"; // Menggunakan nama untuk admin

                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, user);
                stmt.setString(2, user);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");

                    // Verifikasi password hanya untuk user
                    if (role.equals("user")) {
                        String dbPassword = rs.getString("password");
                        if (!pass.equals(dbPassword)) {
                            Toast.makeText(this, "Password salah!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // Login berhasil
                    Toast.makeText(this, "Login berhasil sebagai " + role, Toast.LENGTH_SHORT).show();

                    if (role.equals("admin")) {
                        Intent intent = new Intent(this, AdminActivity.class);
                        intent.putExtra("username", user); // Kirim username ke AdminActivity
                        startActivity(intent);
                    } else if (role.equals("user")) {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("username", user); // Kirim username ke HomeActivity
                        startActivity(intent);
                    }
                    finish(); // Tutup LoginActivity
                } else {
                    Toast.makeText(this, "Username tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan logika tombol Register
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
