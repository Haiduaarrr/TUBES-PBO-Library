package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
            String user = username.getText().toString();
            String pass = password.getText().toString();

            if (!user.isEmpty() && !pass.isEmpty()) {
                // Akses SharedPreferences untuk memeriksa kredensial
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String savedUsername = sharedPreferences.getString("username", null);
                String savedPassword = sharedPreferences.getString("password", null);
                String savedRole = sharedPreferences.getString("role", null); // Mengakses role

                // Verifikasi username dan password
                if (savedUsername != null && savedPassword != null && savedUsername.equals(user) && savedPassword.equals(pass)) {
                    // Login berhasil
                    Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                    // Berdasarkan role, navigasi ke halaman yang sesuai
                    if ("admin".equals(savedRole)) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else if ("user".equals(savedRole)) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Role tidak valid!", Toast.LENGTH_SHORT).show();
                    }
                    finish(); // Tutup LoginActivity agar tidak bisa kembali ke login menggunakan tombol back
                } else {
                    Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Username dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tambahkan logika tombol Register
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
