package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StartingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        // Akses elemen layout
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Navigasi ke LoginActivity
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(StartingPageActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Navigasi ke RegisterActivity
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(StartingPageActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
