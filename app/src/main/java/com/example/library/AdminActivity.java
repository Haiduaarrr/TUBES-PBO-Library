package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Akses elemen-elemen UI
        Button btnManageBooks = findViewById(R.id.btnManageBooks);
        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Logika tombol Logout
        btnLogout.setOnClickListener(view -> {
            // Logout dan kembali ke halaman Login
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Tutup AdminActivity
        });

        // Logika tombol Manage Books
        btnManageBooks.setOnClickListener(view -> {
            // Tambahkan navigasi untuk mengelola buku
            // Misalnya, buka halaman baru untuk mengelola buku
            Intent intent = new Intent(AdminActivity.this, ManageBooksActivity.class);
            startActivity(intent);
        });

        // Logika tombol Manage Users
        btnManageUsers.setOnClickListener(view -> {
            // Tambahkan navigasi untuk mengelola pengguna
            // Misalnya, buka halaman baru untuk mengelola pengguna
            Intent intent = new Intent(AdminActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });
    }
}
