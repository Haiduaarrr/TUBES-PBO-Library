package com.example.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ManageBooksActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_books);

        // Referensi tombol
        Button btnAddBook = findViewById(R.id.btnAddBook);
        Button btnBack = findViewById(R.id.btnBack);

        // Inisialisasi RecyclerView
        rvBooks = findViewById(R.id.rvBooks);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(bookList);
        rvBooks.setAdapter(bookAdapter);

        // Listener untuk tombol Add Book
        btnAddBook.setOnClickListener(view -> openAddBookDialog());

        // Listener untuk tombol Back
        btnBack.setOnClickListener(v -> finish());

        // Muat daftar buku dari database
        loadBooksFromDatabase();
    }

    // Membuka dialog untuk menambahkan buku
    private void openAddBookDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_book, null);

        EditText etTitle = dialogView.findViewById(R.id.etBookTitle);
        EditText etAuthor = dialogView.findViewById(R.id.etBookAuthor);
        EditText etStock = dialogView.findViewById(R.id.etBookStock);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add New Book")
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = etTitle.getText().toString();
                    String author = etAuthor.getText().toString();
                    String stockStr = etStock.getText().toString();

                    if (title.isEmpty() || author.isEmpty() || stockStr.isEmpty()) {
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int stock;
                    try {
                        stock = Integer.parseInt(stockStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Stock must be a number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    addBookToDatabase(title, author, stock);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Fungsi untuk menambahkan buku ke database
    private void addBookToDatabase(String title, String author, int stock) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "INSERT INTO book (title, author, stock) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setInt(3, stock);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                loadBooksFromDatabase(); // Segarkan daftar buku
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Memuat daftar buku dari database
    private void loadBooksFromDatabase() {
        bookList.clear();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT * FROM book";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");

                bookList.add(new Book(id, title, author, stock));
            }

            bookAdapter.notifyDataSetChanged(); // Perbarui tampilan RecyclerView
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load books: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
