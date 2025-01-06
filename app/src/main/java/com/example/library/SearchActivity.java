package com.example.library;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private BookAdapter bookAdapter;
    private List<Book> bookList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Inisialisasi RecyclerView
        rvSearchResults = findViewById(R.id.rvSearchResults);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(bookList);
        rvSearchResults.setAdapter(bookAdapter);

        // Input untuk pencarian
        EditText etSearch = findViewById(R.id.etSearch);

        // Tambahkan listener untuk mendeteksi perubahan teks
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Tidak digunakan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Memperbarui hasil pencarian berdasarkan teks yang dimasukkan
                searchBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Tidak digunakan
            }
        });

        // Muat data awal (semua buku)
        loadAllBooks();

        // Tombol Back
        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());
    }

    // Fungsi untuk memuat semua buku dari database
    private void loadAllBooks() {
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

            bookAdapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fungsi untuk mencari buku berdasarkan judul
    private void searchBooks(String queryText) {
        bookList.clear();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT * FROM book WHERE title LIKE ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + queryText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int stock = rs.getInt("stock");

                bookList.add(new Book(id, title, author, stock));
            }

            bookAdapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
