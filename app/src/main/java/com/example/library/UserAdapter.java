package com.example.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtUserName.setText(user.getName());

        String transactions = formatTransactions(user.getTransactions());
        holder.txtUserTransactions.setText(transactions);

        // Debug log data yang diisi ke ViewHolder
        System.out.println("Set data ke ViewHolder: Nama=" + user.getName() + ", Transaksi=" + transactions);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private String formatTransactions(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        for (Transaction t : transactions) {
            sb.append("Title: ").append(t.getBookTitle())
                    .append("\nDate: ").append(t.getTransactionDate())
                    .append("\nStatus: ").append(t.getStatus()).append("\n\n");
        }
        return sb.toString();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtUserTransactions;

        UserViewHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserTransactions = itemView.findViewById(R.id.txtUserTransactions);
        }
    }
}
