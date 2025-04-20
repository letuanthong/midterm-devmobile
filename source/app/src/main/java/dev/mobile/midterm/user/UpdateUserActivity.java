package dev.mobile.midterm.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.User;

public class UpdateUserActivity extends AppCompatActivity {
    private EditText updateUserName, updateUserEmail, updateUserPhoneNumber, updateUserStatus, updateUserRole;
    private Button updateUserBtn;
    private FirebaseFirestore db;
    private static final String TAG = "UpdateUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        updateUserName = findViewById(R.id.updateUserName);
        updateUserEmail = findViewById(R.id.updateUserEmail);
        updateUserPhoneNumber = findViewById(R.id.updateUserPhoneNumber);
        updateUserStatus = findViewById(R.id.updateUserStatus);
        updateUserRole = findViewById(R.id.updateUserRole);
        updateUserBtn = findViewById(R.id.updateUserBtn);

        db = FirebaseFirestore.getInstance();

        // Get user ID from intent
        String userId = getIntent().getStringExtra("Key");
        if (userId != null) {
            loadUserData(userId);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int indexSelected = 1;

        // Set focus change listeners for Status and Role fields
        updateUserStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"Block", "Normal"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    updateUserStatus.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        updateUserRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"admin", "manager", "employee"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    updateUserRole.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        updateUserBtn.setOnClickListener(v -> updateUser(userId));
    }

    private void loadUserData(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            updateUserName.setText(user.getName());
                            updateUserEmail.setText(user.getEmail());
                            updateUserPhoneNumber.setText(user.getPhone());
                            updateUserStatus.setText(user.getStatus());
                            updateUserRole.setText(user.getRole());
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateUser(String userId) {
        String name = updateUserName.getText().toString();
        String email = updateUserEmail.getText().toString();
        String phoneNumber = updateUserPhoneNumber.getText().toString();
        String status = updateUserStatus.getText().toString();
        String role = updateUserRole.getText().toString();

        db.collection("users").document(userId)
                .update("name", name, "email", email, "phoneNumber", phoneNumber, "status", status, "role", role)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateUserActivity.this, ViewUsersActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateUserActivity.this, "Error updating user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}