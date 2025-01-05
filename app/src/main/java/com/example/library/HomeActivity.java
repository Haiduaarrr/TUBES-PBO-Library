package com.example.library;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Akses elemen header
        TextView userName = findViewById(R.id.tvUserName);
        TextView userLocation = findViewById(R.id.tvUserLocation);

        // Akses SharedPreferences untuk mengambil username
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", null);

        if (savedUsername != null) {
            userName.setText(savedUsername);
            userLocation.setText("Bandung, Indonesia");
        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Setup RecyclerView untuk Popular Books
        RecyclerView rvPopularBooks = findViewById(R.id.rvPopularBooks);
        List<String> popularBooks = Arrays.asList("Sherlock Holmes", "Morning Star", "Ther Merlian");
        BookAdapter popularAdapter = new BookAdapter(this, popularBooks);
        rvPopularBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvPopularBooks.setAdapter(popularAdapter);

        // Setup RecyclerView untuk Learn Books
        RecyclerView rvLearnBooks = findViewById(R.id.rvLearnBooks);
        List<String> learnBooks = Arrays.asList("SQL", "Arduino", "Pemrograman Web");
        BookAdapter learnAdapter = new BookAdapter(this, learnBooks);
        rvLearnBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvLearnBooks.setAdapter(learnAdapter);

        // Bottom Navigation Bar
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navSearch = findViewById(R.id.borrowed);
        ImageView navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(view -> {
            Log.d("HomeActivity", "Home icon clicked");
            // Stay on Home
        });

        navSearch.setOnClickListener(view -> {
            Log.d("HomeActivity", "Search icon clicked");
            Intent borrowedIntent = new Intent(HomeActivity.this, BorrowedBooksActivity.class);
            startActivity(borrowedIntent);
        });

        navProfile.setOnClickListener(view -> {
            Log.d("HomeActivity", "Profile icon clicked");
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        });
    }
}
