package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Akses elemen dari layout
        EditText username = findViewById(R.id.etUsername);
        EditText password = findViewById(R.id.etPassword);
        EditText confirmPassword = findViewById(R.id.etConfirmPassword);
        RadioGroup roleGroup = findViewById(R.id.roleGroup); // RadioGroup
        Button btnNext = findViewById(R.id.btnNext);

        // Tambahkan logika tombol Next
        btnNext.setOnClickListener(view -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)) {
                Toast.makeText(RegisterActivity.this, "Password tidak sesuai", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password harus minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil role yang dipilih
            int selectedRoleId = roleGroup.getCheckedRadioButtonId();
            if (selectedRoleId != -1) {
                RadioButton selectedRoleButton = findViewById(selectedRoleId);
                String role = selectedRoleButton.getTag().toString();

                // Simpan username, password, dan role ke SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", user);
                editor.putString("password", pass); // Simpan password (gunakan hash jika diperlukan)
                editor.putString("role", role); // Simpan role
                editor.apply();

                // Debugging
                System.out.println("Username: " + user);
                System.out.println("Role: " + role);

                // Tampilkan pesan registrasi berhasil
                Toast.makeText(RegisterActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();

                // Pindah ke LoginActivity setelah registrasi sukses
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Menutup RegisterActivity
            } else {
                Toast.makeText(RegisterActivity.this, "Pilih role terlebih dahulu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
