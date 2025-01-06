package com.example.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        // Setup RecyclerView untuk Daftar Buku
        RecyclerView rvBooks = findViewById(R.id.rvPopularBooks);
        List<Book> books = fetchBooksFromDatabase();

        BookAdapter bookAdapter = new BookAdapter(books);
        rvBooks.setLayoutManager(new GridLayoutManager(this, 2));
        rvBooks.setAdapter(bookAdapter);

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

    private List<Book> fetchBooksFromDatabase() {
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT book_id, title, author, stock FROM book";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");

                books.add(new Book(id, title, author, stock));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

}
