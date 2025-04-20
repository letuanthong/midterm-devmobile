package dev.mobile.midterm.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.User;

public class AddUserActivity extends AppCompatActivity {

    TextInputEditText editTextName, editTextPhoneNumber, editTextEmail, editTextPassword, inputStatus, inputRole;
    Button addUserBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    private static final String TAG = "AddUserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        editTextName = findViewById(R.id.inputName);
        editTextPhoneNumber = findViewById(R.id.inputPhoneNumber);
        editTextEmail = findViewById(R.id.inputEmail);
        editTextPassword = findViewById(R.id.inputPassword);
        inputStatus = findViewById(R.id.inputStatus);
        inputRole = findViewById(R.id.inputRole);
        addUserBtn = findViewById(R.id.saveUserBtn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int indexSelected = 1;

        // Set focus change listeners for Status and Role fields
        inputStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"Block", "Normal"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    inputStatus.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        inputRole.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"admin", "manager", "employee"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    inputRole.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        addUserBtn.setOnClickListener(v -> {
            String name = String.valueOf(editTextName.getText());
            String phoneNumber = String.valueOf(editTextPhoneNumber.getText());
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String status = String.valueOf(inputStatus.getText());
            String role = String.valueOf(inputRole.getText());

            // Check if any field is empty
            if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty() || status.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate phone number
            if (!phoneNumber.matches("\\d+")) {
                Toast.makeText(this, "Phone number must be a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password length
            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                User user = new User(uid, email, name, phoneNumber, role, status);

                                db.collection("users").document(uid).set(user)
                                        .addOnCompleteListener(dbTask -> {
                                            if (dbTask.isSuccessful()) {
                                                Toast.makeText(this, "Add new user successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Log.e(TAG, "Database write failed: " + dbTask.getException().getMessage());
                                                Toast.makeText(this, "Database write failed: " + dbTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(e -> {
                                            Log.e(TAG, "Database write failed: " + e.getMessage());
                                            Toast.makeText(this, "Database write failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Log.e(TAG, "Authentication failed: " + task.getException().getMessage());
                            Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
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