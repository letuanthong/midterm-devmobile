package dev.mobile.midterm.student;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.firestore.FirebaseFirestore;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Student;

public class UpdateStudentActivity extends AppCompatActivity {
    private EditText updateStudentId, updateStudentName, updateStudentAge, updateStudentPhoneNumber, updateStudentStatus, updateStudentMajor, updateStudentClass, updateStudentJoinedDate, updateStudentAvgScore;
    private Button updateStudentBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        updateStudentId = findViewById(R.id.updateStudentId);
        updateStudentName = findViewById(R.id.updateStudentName);
        updateStudentAge = findViewById(R.id.updateStudentAge);
        updateStudentPhoneNumber = findViewById(R.id.updateStudentPhoneNumber);
        updateStudentStatus = findViewById(R.id.updateStudentStatus);
        updateStudentMajor = findViewById(R.id.updateStudentMajor);
        updateStudentClass = findViewById(R.id.updateStudentClass);
        updateStudentJoinedDate = findViewById(R.id.updateStudentJoinedDate);
        updateStudentAvgScore = findViewById(R.id.updateStudentAvgScore);
        updateStudentBtn = findViewById(R.id.updateStudentBtn);

        db = FirebaseFirestore.getInstance();

        // Get student ID from intent
        String studentId = getIntent().getStringExtra("id");
        if (studentId != null) {
            loadStudentData(studentId);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateStudentActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int indexSelected = 1;

        updateStudentAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        month = month + 1; // Month is 0-based in DatePicker
                        updateStudentAge.setText(dayOfMonth + "/" + month + "/" + year);
                    });
                }
            }
        });

        updateStudentMajor.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {
                        "Information Technology",
                        "Computer Science",
                        "Software Engineering ",
                        "Cybersecurity",
                        "Data Science",
                        "Artificial Intelligence"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    updateStudentMajor.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        updateStudentStatus.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String[] items = {"active", "graduated", "dropout", "suspended", "on_leave", "transferred"};
                builder.setSingleChoiceItems(items, indexSelected, (dialog, which) -> {
                    updateStudentStatus.setText(items[which]);
                    dialog.dismiss();
                });
                builder.show();
            }
        });

        updateStudentBtn.setOnClickListener(v -> updateStudent(studentId));
    }

    private void loadStudentData(String studentId) {
        db.collection("students").document(studentId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        if (student != null) {
                            updateStudentId.setText(student.getStudentId());
                            updateStudentName.setText(student.getName());
                            updateStudentAge.setText(student.getDateOfBirth());
                            updateStudentPhoneNumber.setText(student.getPhone());
                            updateStudentStatus.setText(student.getStatus());
                            updateStudentMajor.setText(student.getMajor());
                            updateStudentClass.setText(student.getClassYear());
                            updateStudentJoinedDate.setText(student.getJoiningDate());
                            updateStudentAvgScore.setText(student.getGpa());
                        }
                    } else {
                        Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading student data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateStudent(String studentId) {
        String studentID = updateStudentId.getText().toString();
        String name = updateStudentName.getText().toString();
        String age = updateStudentAge.getText().toString();
        String phoneNumber = updateStudentPhoneNumber.getText().toString();
        String status = updateStudentStatus.getText().toString();
        String major = updateStudentMajor.getText().toString();
        String classYear = updateStudentClass.getText().toString();
        String joinedDate = updateStudentJoinedDate.getText().toString();
        String avgScore = updateStudentAvgScore.getText().toString();

        db.collection("students").document(studentId)
                .update("studentId", studentID, "name", name, "dateOfBirth", age, "phone", phoneNumber, "status", status, "major", major, "classYear", classYear, "joiningDate", joinedDate, "gpa", avgScore)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateStudentActivity.this, "Student updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateStudentActivity.this, ViewStudentsActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(UpdateStudentActivity.this, "Error updating student: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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