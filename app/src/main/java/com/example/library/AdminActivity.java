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
        Button btnPinjamBuku = findViewById(R.id.btnPinjamBuku); // Tombol Pinjam Buku
        Button btnKembalikanBuku = findViewById(R.id.btnKembalikanBuku);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Logika tombol Logout
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Tutup AdminActivity
        });

        // Logika tombol Manage Books
        btnManageBooks.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ManageBooksActivity.class);
            startActivity(intent);
        });

        // Logika tombol Manage Users
        btnManageUsers.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, ManageUsersActivity.class);
            startActivity(intent);
        });

        // Logika tombol Pinjam Buku
        btnPinjamBuku.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AdminRequestActivity.class);
            startActivity(intent);
        });

        // Logika tombol Kembalikan Buku
        btnKembalikanBuku.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this,  AdminReturnActivity.class);
            startActivity(intent);
        });
    }
}
