package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.BreakIterator;

public class RegisterActivity extends AppCompatActivity {

    private static final String ADMIN_CODE = "ADMIN123"; // Kode untuk Admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText username = findViewById(R.id.etUsername);
        EditText password = findViewById(R.id.etPassword);
        EditText confirmPassword = findViewById(R.id.etConfirmPassword);
        EditText nama = findViewById(R.id.etNama);
        EditText phoneNumber = findViewById(R.id.etPhoneNumber);
        EditText adminCodeInput = findViewById(R.id.etAdminCode); // Tambahan untuk kode admin
        RadioGroup roleGroup = findViewById(R.id.roleGroup);
        Button btnRegister = findViewById(R.id.btnNext);

        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            EditText adminCodeInputt = findViewById(R.id.etAdminCode);
            if (checkedId == R.id.rbAdmin) {
                adminCodeInput.setVisibility(View.VISIBLE); // Tampilkan input kode admin
            } else {
                adminCodeInput.setVisibility(View.GONE); // Sembunyikan input kode admin
            }
        });


        btnRegister.setOnClickListener(view -> {
            String user = username.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();
            String fullName = nama.getText().toString().trim();
            String phone = phoneNumber.getText().toString().trim();

            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(confirmPass)) {
                Toast.makeText(this, "Password tidak sesuai!", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedRoleId = roleGroup.getCheckedRadioButtonId();
            if (selectedRoleId == -1) {
                Toast.makeText(this, "Pilih role terlebih dahulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRoleButton = findViewById(selectedRoleId);
            String role = selectedRoleButton.getTag().toString(); // "user" atau "admin"

            if (role.equals("admin")) {
                String adminCode = adminCodeInput.getText().toString().trim();
                if (adminCode.isEmpty()) {
                    Toast.makeText(this, "Masukkan kode admin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!adminCode.equals(ADMIN_CODE)) {
                    Toast.makeText(this, "Kode admin salah!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            try (Connection connection = DatabaseConnection.connect()) {
                // Insert data ke tabel `person`
                String personQuery = "INSERT INTO person (nama, phone_number) VALUES (?, ?)";
                PreparedStatement personStmt = connection.prepareStatement(personQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                personStmt.setString(1, fullName);
                personStmt.setString(2, phone);
                personStmt.executeUpdate();

                ResultSet rs = personStmt.getGeneratedKeys();
                if (rs.next()) {
                    int personId = rs.getInt(1);

                    if (role.equals("user")) {
                        // Insert ke tabel `user`
                        String userQuery = "INSERT INTO user (person_id, username, password) VALUES (?, ?, ?)";
                        PreparedStatement userStmt = connection.prepareStatement(userQuery);
                        userStmt.setInt(1, personId);
                        userStmt.setString(2, user);
                        userStmt.setString(3, pass);
                        userStmt.executeUpdate();

                    } else if (role.equals("admin")) {
                        // Insert ke tabel `admin`
                        String adminQuery = "INSERT INTO admin (person_id) VALUES (?)";
                        PreparedStatement adminStmt = connection.prepareStatement(adminQuery);
                        adminStmt.setInt(1, personId);
                        adminStmt.executeUpdate();
                    }

                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
