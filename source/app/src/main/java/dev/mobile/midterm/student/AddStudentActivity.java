package dev.mobile.midterm.student;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Student;

public class AddStudentActivity extends AppCompatActivity {

    private EditText inputStudentId, inputStudentName, inputStudentAge, inputStudentPhoneNumber, inputStudentStatus, inputStudentMajor, inputStudentClass, inputStudentJoinedDate, inputStudentAvgScore;
    private Button addStudentBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        inputStudentId = findViewById(R.id.inputStudentId);
        inputStudentName = findViewById(R.id.inputStudentName);
        inputStudentAge = findViewById(R.id.inputStudentAge);
        inputStudentPhoneNumber = findViewById(R.id.inputStudentPhoneNumber);
        inputStudentStatus = findViewById(R.id.inputStudentStatus);
        inputStudentMajor = findViewById(R.id.inputStudentMajor);
        inputStudentClass = findViewById(R.id.inputStudentClass);
        inputStudentJoinedDate = findViewById(R.id.inputStudentJoinedDate);
        inputStudentAvgScore = findViewById(R.id.inputStudentAvgScore);
        addStudentBtn = findViewById(R.id.addStudentBtn);

        db = FirebaseFirestore.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddStudentActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int indexSelected = 1;

        inputStudentAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        month = month + 1; // Month is 0-based in DatePicker
                        inputStudentAge.setText(dayOfMonth + "/" + month + "/" + year);
                    });
                }
            }
        });

        inputStudentMajor.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {
                        "Information Technology",
                        "Computer Science",
                        "Software Engineering ",
                        "Cybersecurity",
                        "Data Science",
                        "Artificial Intelligence"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    inputStudentMajor.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        inputStudentStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"active", "graduated", "dropout", "suspended", "on_leave", "transferred"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    inputStudentStatus.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        addStudentBtn.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();
            String studentId = inputStudentId.getText().toString();
            String name = inputStudentName.getText().toString();
            String age = inputStudentAge.getText().toString();
            String phoneNumber = inputStudentPhoneNumber.getText().toString();
            String status = inputStudentStatus.getText().toString();
            String major = inputStudentMajor.getText().toString();
            String classYear = inputStudentClass.getText().toString();
            String joinedDate = inputStudentJoinedDate.getText().toString();
            String avgScore = inputStudentAvgScore.getText().toString();

            // Validate inputs
            if (studentId.isEmpty() || name.isEmpty() || age.isEmpty() || phoneNumber.isEmpty() || status.isEmpty() || major.isEmpty() || classYear.isEmpty() || joinedDate.isEmpty() || avgScore.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!phoneNumber.matches("\\d+")) {
                Toast.makeText(this, "Phone number must be a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!avgScore.matches("\\d+(\\.\\d+)?")) {
                Toast.makeText(this, "Average score must be a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new student object
            Student student = new Student(id, studentId, name, age, phoneNumber, status, major, classYear, joinedDate, avgScore);

            // Save to Firestore
            db.collection("students").document(id).set(student)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error adding student: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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