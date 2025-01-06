package com.example.library;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminRequestActivity extends AppCompatActivity {

    private AutoCompleteTextView actUser, actBook;
    private EditText etBorrowDate, etReturnDate;
    private Button btnAccept, btnReject, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        // Inisialisasi elemen UI
        actUser = findViewById(R.id.actUser);
        actBook = findViewById(R.id.actBook);
        etBorrowDate = findViewById(R.id.etBorrowDate);
        etReturnDate = findViewById(R.id.etReturnDate);
        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);
        btnBack = findViewById(R.id.btnBack);

        // Load data user dan buku dari database
        loadUserData();
        loadBookData();

        // Logika tombol Accept
        btnAccept.setOnClickListener(view -> {
            String user = actUser.getText().toString().trim();
            String book = actBook.getText().toString().trim();
            String borrowDate = etBorrowDate.getText().toString().trim();
            String returnDate = etReturnDate.getText().toString().trim();

            if (user.isEmpty() || book.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom!", Toast.LENGTH_SHORT).show();
            } else {
                // Simpan ke database
                saveTransaction(user, book, borrowDate, returnDate);
            }
        });

        // Logika tombol Reject
        btnReject.setOnClickListener(view -> {
            Toast.makeText(this, "Permintaan ditolak.", Toast.LENGTH_LONG).show();
            finish();
        });

        // Logika tombol Back
        btnBack.setOnClickListener(view -> finish());
    }

    private void loadUserData() {
        ArrayList<String> users = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT username FROM user";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString("username"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, users);
            actUser.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat data user.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadBookData() {
        ArrayList<String> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT title FROM book";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(rs.getString("title"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, books);
            actBook.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat data buku.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTransaction(String user, String book, String borrowDate, String returnDate) {
        try (Connection connection = DatabaseConnection.connect()) {
            connection.setAutoCommit(false);

            // Query untuk mendapatkan user_id dan book_id
            String userQuery = "SELECT user_id FROM user WHERE username = ?";
            String bookQuery = "SELECT book_id, stock FROM book WHERE title = ?";
            PreparedStatement userStmt = connection.prepareStatement(userQuery);
            PreparedStatement bookStmt = connection.prepareStatement(bookQuery);

            userStmt.setString(1, user);
            bookStmt.setString(1, book);

            ResultSet userRs = userStmt.executeQuery();
            ResultSet bookRs = bookStmt.executeQuery();

            if (userRs.next() && bookRs.next()) {
                int userId = userRs.getInt("user_id");
                int bookId = bookRs.getInt("book_id");
                int stock = bookRs.getInt("stock");

                if (stock > 0) {
                    String insertTransactionQuery = "INSERT INTO transaction (user_id, book_id, transaction_date, return_date, status) " +
                            "VALUES (?, ?, ?, ?, 'borrowed')";
                    PreparedStatement insertTransactionStmt = connection.prepareStatement(insertTransactionQuery);
                    insertTransactionStmt.setInt(1, userId);
                    insertTransactionStmt.setInt(2, bookId);
                    insertTransactionStmt.setString(3, borrowDate);
                    insertTransactionStmt.setString(4, returnDate);

                    String updateBookStockQuery = "UPDATE book SET stock = stock - 1 WHERE book_id = ?";
                    PreparedStatement updateBookStockStmt = connection.prepareStatement(updateBookStockQuery);
                    updateBookStockStmt.setInt(1, bookId);

                    insertTransactionStmt.executeUpdate();
                    updateBookStockStmt.executeUpdate();

                    connection.commit();
                    Toast.makeText(this, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Stok buku habis!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User atau buku tidak ditemukan!", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan transaksi.", Toast.LENGTH_SHORT).show();
        }
    }
}
