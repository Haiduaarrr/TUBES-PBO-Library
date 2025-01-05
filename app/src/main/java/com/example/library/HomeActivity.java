package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        // Ambil username dari intent yang dikirim dari LoginActivity
        String loggedInUsername = getIntent().getStringExtra("username");

        if (loggedInUsername != null) {
            try (Connection connection = DatabaseConnection.connect()) {
                // Query untuk mendapatkan detail pengguna dari database
                String query = "SELECT p.nama FROM user u JOIN person p ON u.person_id = p.person_id WHERE u.username = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, loggedInUsername);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("nama");
                    userName.setText(fullName); // Tampilkan nama pengguna
                    userLocation.setText("Bandung, Indonesia");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                userName.setText("Error");
                userLocation.setText("Unknown Location");
            }
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
        ImageView navSearch = findViewById(R.id.navSearch);
        ImageView navProfile = findViewById(R.id.navProfile);

        navHome.setOnClickListener(view -> {
            // Stay on Home
        });

        navSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(searchIntent);
        });

        navProfile.setOnClickListener(view -> {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            profileIntent.putExtra("username", loggedInUsername); // Kirim username ke ProfileActivity
            startActivity(profileIntent);
        });
    }
}
