package dev.mobile.midterm.certificate;

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
import dev.mobile.midterm.model.Certificate;
import dev.mobile.midterm.student.AddStudentActivity;

public class AddCertificateActivity extends AppCompatActivity {
    private EditText inputCertificateName, inputCertificateCores, inputCertificateIssueDate, inputCertificateExpiryDate;
    private Button addCertificateBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        inputCertificateName = findViewById(R.id.inputCertificateName);
        inputCertificateCores = findViewById(R.id.inputCertificateCores);
        inputCertificateIssueDate = findViewById(R.id.inputCertificateIssueDate);
        inputCertificateExpiryDate = findViewById(R.id.inputCertificateExpiryDate);
        addCertificateBtn = findViewById(R.id.addCertificateBtn);

        db = FirebaseFirestore.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddCertificateActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int indexSelected = 1;

        inputCertificateIssueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        month = month + 1; // Month is 0-based in DatePicker
                        inputCertificateIssueDate.setText(dayOfMonth + "/" + month + "/" + year);
                    });
                }
            }
        });

        inputCertificateExpiryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        month = month + 1; // Month is 0-based in DatePicker
                        inputCertificateExpiryDate.setText(dayOfMonth + "/" + month + "/" + year);
                    });
                }
            }
        });

        addCertificateBtn.setOnClickListener(v -> {
            String id = UUID.randomUUID().toString();
            String studentId = getIntent().getStringExtra("studentId");
            String name = inputCertificateName.getText().toString();
            String cores = inputCertificateCores.getText().toString();
            String issueDate = inputCertificateIssueDate.getText().toString();
            String expiryDate = inputCertificateExpiryDate.getText().toString();

            if (name.isEmpty() || cores.isEmpty() || issueDate.isEmpty() || expiryDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Certificate certificate = new Certificate(id, name, studentId, cores, issueDate, expiryDate);
            db.collection("certificates").document(id).set(certificate)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Certificate added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error adding certificate", Toast.LENGTH_SHORT).show();
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
