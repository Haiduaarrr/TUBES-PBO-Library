package com.example.library;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ManageBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books); // Pastikan layout activity_manage_books ada

        // Referensi ke button Back
        Button btnBack = findViewById(R.id.btnBack);

        // Set listener untuk button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi kembali ke halaman sebelumnya
                finish(); // Menutup aktivitas saat ini dan kembali ke aktivitas sebelumnya
            }
        });
    }
}
