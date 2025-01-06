package com.example.library;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminReturnActivity extends AppCompatActivity {

    private AutoCompleteTextView actUser, actBook;
    private Button btnReturn, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_return);

        // Inisialisasi elemen UI
        actUser = findViewById(R.id.actUser);
        actBook = findViewById(R.id.actBook);
        btnReturn = findViewById(R.id.btnReturn);
        btnBack = findViewById(R.id.btnBack);

        // Load data user dan buku yang sedang dipinjam
        loadUserData();
        actUser.setOnItemClickListener((parent, view, position, id) -> loadBorrowedBooks());

        // Logika tombol Return
        btnReturn.setOnClickListener(view -> {
            String user = actUser.getText().toString().trim();
            String book = actBook.getText().toString().trim();

            if (user.isEmpty() || book.isEmpty()) {
                Toast.makeText(this, "Harap pilih user dan buku!", Toast.LENGTH_SHORT).show();
            } else {
                returnBook(user, book);
            }
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

    private void loadBorrowedBooks() {
        ArrayList<String> books = new ArrayList<>();
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT b.title FROM transaction t " +
                    "JOIN book b ON t.book_id = b.book_id " +
                    "JOIN user u ON t.user_id = u.user_id " +
                    "WHERE u.username = ? AND t.status = 'borrowed'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, actUser.getText().toString().trim());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(rs.getString("title"));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, books);
            actBook.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal memuat buku yang dipinjam.", Toast.LENGTH_SHORT).show();
        }
    }

    private void returnBook(String user, String book) {
        try (Connection connection = DatabaseConnection.connect()) {
            connection.setAutoCommit(false);

            // Mendapatkan transaction_id dan book_id
            String query = "SELECT t.transaction_id, b.book_id FROM transaction t " +
                    "JOIN book b ON t.book_id = b.book_id " +
                    "JOIN user u ON t.user_id = u.user_id " +
                    "WHERE u.username = ? AND b.title = ? AND t.status = 'borrowed'";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, user);
            stmt.setString(2, book);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int bookId = rs.getInt("book_id");

                // Update status transaksi dan stok buku
                String updateTransaction = "UPDATE transaction SET status = 'returned', return_date = CURDATE() WHERE transaction_id = ?";
                String updateBookStock = "UPDATE book SET stock = stock + 1 WHERE book_id = ?";
                PreparedStatement stmtUpdateTransaction = connection.prepareStatement(updateTransaction);
                PreparedStatement stmtUpdateBookStock = connection.prepareStatement(updateBookStock);

                stmtUpdateTransaction.setInt(1, transactionId);
                stmtUpdateBookStock.setInt(1, bookId);

                stmtUpdateTransaction.executeUpdate();
                stmtUpdateBookStock.executeUpdate();

                connection.commit();
                Toast.makeText(this, "Buku berhasil dikembalikan!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Buku tidak ditemukan atau sudah dikembalikan.", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mengembalikan buku.", Toast.LENGTH_SHORT).show();
        }
    }
}
