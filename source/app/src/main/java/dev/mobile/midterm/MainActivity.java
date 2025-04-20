package dev.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dev.mobile.midterm.student.AddStudentActivity;
import dev.mobile.midterm.student.ViewStudentsActivity;
import dev.mobile.midterm.user.AddUserActivity;
import dev.mobile.midterm.user.ViewUsersActivity;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView userTextView;
    private Button addUserBtn, viewUserList, addStudentBtn, viewStudentListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout accountManagementLayout = findViewById(R.id.accountsLayout);

        addUserBtn = findViewById(R.id.addUserBtn);
        viewUserList = findViewById(R.id.viewListUsersBtn);
        addStudentBtn = findViewById(R.id.addStudentBtn);
        viewStudentListBtn = findViewById(R.id.viewListStudentsBtn);

        viewUserList.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewUsersActivity.class);
            startActivity(intent);
        });

        addUserBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });

        viewStudentListBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStudentsActivity.class);
            startActivity(intent);
        });

        addStudentBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userTextView = findViewById(R.id.userTextView);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String role = document.getString("role");
                                if (getSupportActionBar() != null) {
                                    userTextView.setVisibility(View.GONE);
                                    getSupportActionBar().setTitle(role);
                                }
                                if (!"admin".equals(role)) {
                                    accountManagementLayout.setVisibility(View.GONE);
                                }
                            } else {
                                userTextView.setText("No role found.");
                            }
                        } else {
                            userTextView.setText("Failed to retrieve role.");
                        }
                    });
        } else {
            userTextView.setText("No user is logged in");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOutBtn) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sign Out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}