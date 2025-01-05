package com.example.library; // Tambahkan ini sesuai dengan nama package Anda

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBooksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books); // Pastikan layout ini ada di res/layout/

        // RecyclerView untuk daftar buku yang dipinjam
        RecyclerView rvBorrowedBooks = findViewById(R.id.rvBorrowedBooks);
        rvBorrowedBooks.setLayoutManager(new LinearLayoutManager(this));

        // Contoh data buku
        List<String> borrowedBooks = new ArrayList<>();
        borrowedBooks.add("Sherlock Holmes");
        borrowedBooks.add("Morning Star");
        borrowedBooks.add("The Martian");

        // Atur adapter untuk RecyclerView
        BookAdapter adapter = new BookAdapter(this, borrowedBooks);
        rvBorrowedBooks.setAdapter(adapter);
    }
}
