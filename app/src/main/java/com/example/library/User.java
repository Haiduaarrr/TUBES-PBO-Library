package com.example.library;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String username;
    private List<Transaction> transactions;

    public User(int id, String name, String username, List<Transaction> transactions) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.transactions = transactions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
