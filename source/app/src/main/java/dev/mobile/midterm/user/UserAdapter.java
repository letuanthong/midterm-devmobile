package dev.mobile.midterm.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.User;
import lombok.NonNull;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private Context mContext;
    private List<User> userList;

    public UserAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userNameTxtView.setText(user.getName());
        holder.userEmailTxtView.setText(user.getEmail());
        holder.userPhoneTxtView.setText(user.getPhone());
        holder.userCard.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UserDetailActivity.class);
            intent.putExtra("Key", user.getId());
            intent.putExtra("Name", user.getName());
            intent.putExtra("Email", user.getEmail());
            intent.putExtra("Phone", user.getPhone());
            intent.putExtra("Role",user.getRole());
            intent.putExtra("Status", user.getStatus());
            mContext.startActivity(intent);
        });

        holder.updateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UpdateUserActivity.class);
            intent.putExtra("Key", user.getId());
            intent.putExtra("Name", user.getName());
            intent.putExtra("Email", user.getEmail());
            intent.putExtra("Phone", user.getPhone());
            intent.putExtra("Role",user.getRole());
            intent.putExtra("Status", user.getStatus());
            mContext.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(user.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(mContext, "Error deleting user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

