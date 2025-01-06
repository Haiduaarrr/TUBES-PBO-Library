package com.example.library;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksActivity extends AppCompatActivity {

    private RecyclerView rvBorrowedBooks;
    private TextView tvEmptyState;
    private BookAdapter bookAdapter;
    private List<Book> borrowedBooks = new ArrayList<>();
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);

        // Inisialisasi RecyclerView, TextView, dan Button
        rvBorrowedBooks = findViewById(R.id.rvBorrowedBooks);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnBack = findViewById(R.id.btnBack);

        rvBorrowedBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(borrowedBooks);
        rvBorrowedBooks.setAdapter(bookAdapter);

        // Tambahkan fungsi tombol Back
        btnBack.setOnClickListener(view -> finish());

        // Load data buku yang dipinjam
        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        borrowedBooks.clear();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT b.book_id, b.title, b.author, b.stock " +
                    "FROM transaction t " +
                    "JOIN book b ON t.book_id = b.book_id " +
                    "WHERE t.status = 'borrowed'";

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");

                borrowedBooks.add(new Book(id, title, author, stock));
            }

            if (borrowedBooks.isEmpty()) {
                tvEmptyState.setVisibility(View.VISIBLE);
                rvBorrowedBooks.setVisibility(View.GONE);
            } else {
                tvEmptyState.setVisibility(View.GONE);
                rvBorrowedBooks.setVisibility(View.VISIBLE);
            }

            bookAdapter.notifyDataSetChanged(); // Update tampilan RecyclerView
        } catch (SQLException e) {
            e.printStackTrace();
            tvEmptyState.setVisibility(View.VISIBLE);
            rvBorrowedBooks.setVisibility(View.GONE);
        }
    }
}
