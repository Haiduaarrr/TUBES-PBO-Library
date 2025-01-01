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
        Button btnRegister = findViewById(R.id.btnRegister);

        // Tambahkan logika tombol Login
        btnLogin.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (!user.isEmpty() && !pass.isEmpty()) {
                // Akses SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                String savedUsername = sharedPreferences.getString("username", null);
                String savedPassword = sharedPreferences.getString("password", null);
                String savedRole = sharedPreferences.getString("role", null);

                // Debugging untuk memastikan data
                System.out.println("Saved Username: " + savedUsername);
                System.out.println("Saved Password: " + savedPassword);
                System.out.println("Saved Role: " + savedRole);

                // Verifikasi username, password, dan role
                if (savedUsername != null && savedPassword != null && savedRole != null &&
                        savedUsername.equals(user) && savedPassword.equals(pass)) {

                    // Berdasarkan role, navigasi ke halaman yang sesuai
                    if ("admin".equalsIgnoreCase(savedRole)) {
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                    } else if ("user".equalsIgnoreCase(savedRole)) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Role tidak valid! Hubungi admin.", Toast.LENGTH_SHORT).show();
                    }
                    finish(); // Tutup LoginActivity
                } else {
                    Toast.makeText(LoginActivity.this, "Username atau password salah!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Username dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Register
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
