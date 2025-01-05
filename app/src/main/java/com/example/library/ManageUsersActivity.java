package com.example.library;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Ambil data pengguna dari database
        List<User> users = getUsersFromDatabase();

        // Debug log jumlah data
        System.out.println("Jumlah user yang diteruskan ke adapter: " + users.size());
        for (User user : users) {
            System.out.println("User: ID=" + user.getId() + ", Nama=" + user.getName());
        }

        userAdapter = new UserAdapter(users);
        recyclerView.setAdapter(userAdapter);
    }

    private List<User> getUsersFromDatabase() {
        List<User> users = new ArrayList<>();

        String query = "SELECT u.user_id, p.nama, u.username, " +
                "t.transaction_id, t.transaction_date, t.return_date, t.status, b.title " +
                "FROM user u " +
                "JOIN person p ON u.person_id = p.person_id " +
                "LEFT JOIN transaction t ON u.user_id = t.user_id " +
                "LEFT JOIN book b ON t.book_id = b.book_id";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/perpustakaan", "root", "password");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            Map<Integer, User> userMap = new HashMap<>();

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("nama");
                String username = resultSet.getString("username");

                System.out.println("Data user: ID=" + userId + ", Nama=" + name + ", Username=" + username);

                // Jika user belum ada di map, tambahkan
                User user = userMap.getOrDefault(userId, new User(userId, name, username, new ArrayList<>()));

                // Tambahkan transaksi jika ada
                int transactionId = resultSet.getInt("transaction_id");
                if (transactionId != 0) {
                    String bookTitle = resultSet.getString("title");
                    String transactionDate = resultSet.getString("transaction_date");
                    String returnDate = resultSet.getString("return_date");
                    String status = resultSet.getString("status");

                    System.out.println("Transaksi: ID=" + transactionId + ", Judul Buku=" + bookTitle + ", Status=" + status);

                    user.getTransactions().add(new Transaction(transactionId, bookTitle, transactionDate, returnDate, status));
                }

                userMap.put(userId, user);
            }

            users.addAll(userMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error dalam koneksi database atau query");
        }

        return users;
    }
}
