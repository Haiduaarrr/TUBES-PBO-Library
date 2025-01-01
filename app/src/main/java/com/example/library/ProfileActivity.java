package com.example.library;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Akses elemen UI
        TextView tvProfileName = findViewById(R.id.tvProfileName);
        TextView tvProfileEmail = findViewById(R.id.tvProfileEmail);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Ambil data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "Guest");
        String savedEmail = sharedPreferences.getString("email", "example@example.com");

        // Tampilkan data pengguna
        tvProfileName.setText(savedUsername);
        tvProfileEmail.setText(savedEmail);

        // Logika Tombol Logout dengan Dialog Konfirmasi
        btnLogout.setOnClickListener(view -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Logout")
                    .setMessage("Apakah Anda yakin ingin logout?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        // Hapus data login dari SharedPreferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("username");
                        editor.apply();

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
