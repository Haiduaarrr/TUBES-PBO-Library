package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
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
            userName.setText(loggedInUsername);
            userLocation.setText("Bandung, Indonesia");
        } else {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Setup RecyclerView untuk Popular Books
        RecyclerView rvPopularBooks = findViewById(R.id.rvPopularBooks);
        List<Book> popularBooks = new ArrayList<>();
        popularBooks.add(new Book(1, "Sherlock Holmes", "Arthur Conan Doyle", 10));
        popularBooks.add(new Book(2, "Morning Star", "Pierce Brown", 5));
        popularBooks.add(new Book(3, "The Martian", "Andy Weir", 7));

        BookAdapter popularAdapter = new BookAdapter(popularBooks);
        rvPopularBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvPopularBooks.setAdapter(popularAdapter);

        // Setup RecyclerView untuk Learn Books
        RecyclerView rvLearnBooks = findViewById(R.id.rvLearnBooks);
        List<Book> learnBooks = new ArrayList<>();
        learnBooks.add(new Book(4, "SQL for Beginners", "John Smith", 8));
        learnBooks.add(new Book(5, "Learn Arduino", "Jane Doe", 6));
        learnBooks.add(new Book(6, "Web Programming", "Michael Lee", 4));

        BookAdapter learnAdapter = new BookAdapter(learnBooks);
        rvLearnBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvLearnBooks.setAdapter(learnAdapter);

        // Bottom Navigation Bar
        ImageView navHome = findViewById(R.id.navHome);
        ImageView navSearch = findViewById(R.id.navSearch);
        ImageView navProfile = findViewById(R.id.navProfile);
        ImageView btnBorrowed = findViewById(R.id.borrowed);

        navHome.setOnClickListener(view -> {
            // Stay on Home
        });

        navSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(searchIntent);
        });

        navProfile.setOnClickListener(view -> {
            Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
            profileIntent.putExtra("username", loggedInUsername);
            startActivity(profileIntent);
        });

        // Tombol untuk melihat daftar buku yang dipinjam
        btnBorrowed.setOnClickListener(view -> {
            Intent borrowedIntent = new Intent(HomeActivity.this, BorrowedBooksActivity.class);
            startActivity(borrowedIntent);
        });
    }
}
