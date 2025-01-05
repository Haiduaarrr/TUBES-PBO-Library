package com.example.library;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Akses elemen UI
        TextView tvProfileName = findViewById(R.id.tvProfileName);
        TextView tvProfileEmail = findViewById(R.id.tvProfileEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Ambil username dari intent yang dikirim dari HomeActivity
        String loggedInUsername = getIntent().getStringExtra("username");

        if (loggedInUsername != null) {
            try (Connection connection = DatabaseConnection.connect()) {
                // Query untuk mendapatkan detail pengguna dari database
                String query = "SELECT p.nama, p.phone_number FROM user u JOIN person p ON u.person_id = p.person_id WHERE u.username = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, loggedInUsername);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("nama");
                    String email = rs.getString("phone_number"); // Contoh email, ubah jika ada kolom email

                    tvProfileName.setText(fullName);
                    tvProfileEmail.setText(email);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                tvProfileName.setText("Error");
                tvProfileEmail.setText("Unknown Email");
            }
        }

        // Logika Tombol Logout dengan Dialog Konfirmasi
        btnLogout.setOnClickListener(view -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Logout")
                    .setMessage("Apakah Anda yakin ingin logout?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Arahkan ke LoginActivity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        });
    }
}
