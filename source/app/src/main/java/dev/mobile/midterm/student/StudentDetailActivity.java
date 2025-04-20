package dev.mobile.midterm.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dev.mobile.midterm.R;
import dev.mobile.midterm.certificate.ViewCertificateActivity;

public class StudentDetailActivity extends AppCompatActivity {
    TextView studentDetailTitle, studentDetailId, studentDetailName, studentDetailDateOfBirth, studentDetailPhoneNumber, studentDetailStatus, studentDetailMajor, studentDetailClass, studentDetailJoinedDate, studentDetailAvgScore;
    Button viewCertificatesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        studentDetailTitle = findViewById(R.id.studentDetailTitle);
        studentDetailId = findViewById(R.id.studentDetailId);
        studentDetailName = findViewById(R.id.studentDetailName);
        studentDetailDateOfBirth = findViewById(R.id.studentDetailAge);
        studentDetailPhoneNumber = findViewById(R.id.studentDetailPhone);
        studentDetailStatus = findViewById(R.id.studentDetailStatus);
        studentDetailMajor = findViewById(R.id.studentDetailMajor);
        studentDetailClass = findViewById(R.id.studentDetailClass);
        studentDetailJoinedDate = findViewById(R.id.studentDetailJoinedDate);
        studentDetailAvgScore = findViewById(R.id.studentDetailAvgScore);
        viewCertificatesBtn = findViewById(R.id.viewCertificatesBtn);

        Intent intent = getIntent();
        studentDetailTitle.setText(intent.getStringExtra("name"));
        studentDetailId.setText(intent.getStringExtra("studentId"));
        studentDetailName.setText(intent.getStringExtra("name"));
        studentDetailDateOfBirth.setText(intent.getStringExtra("dateOfBirth"));
        studentDetailPhoneNumber.setText(intent.getStringExtra("phone"));
        studentDetailStatus.setText(intent.getStringExtra("status"));
        studentDetailMajor.setText(intent.getStringExtra("major"));
        studentDetailClass.setText(intent.getStringExtra("classYear"));
        studentDetailJoinedDate.setText(intent.getStringExtra("joiningDate"));
        studentDetailAvgScore.setText(intent.getStringExtra("gpa"));

        viewCertificatesBtn.setOnClickListener(v -> {
            Intent certificateIntent = new Intent(StudentDetailActivity.this, ViewCertificateActivity.class);
            certificateIntent.putExtra("studentId", intent.getStringExtra("studentId"));
            startActivity(certificateIntent);
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
