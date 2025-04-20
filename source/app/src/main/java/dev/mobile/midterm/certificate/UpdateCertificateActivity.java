package dev.mobile.midterm.certificate;

import android.app.Activity;
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

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Certificate;

public class UpdateCertificateActivity extends AppCompatActivity {
    private EditText updateCertificateId ,updateCertificateName, updateCertificateCore, updateCertificateIssueDate, updateCertificateExpiryDate;
    private Button updateCertificateBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_certificate);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        updateCertificateId = findViewById(R.id.updateCertificateId);
        updateCertificateName = findViewById(R.id.updateCertificateName);
        updateCertificateCore = findViewById(R.id.updateCertificateCores);
        updateCertificateIssueDate = findViewById(R.id.updateCertificateIssueDate);
        updateCertificateExpiryDate = findViewById(R.id.updateCertificateExpiryDate);
        updateCertificateBtn = findViewById(R.id.updateCertificateBtn);

        db = FirebaseFirestore.getInstance();

        // Get certificate ID from intent
        String certificateId = getIntent().getStringExtra("id");
        if (certificateId != null) {
            loadCertificateData(certificateId);
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateCertificateActivity.this);

        updateCertificateIssueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        updateCertificateIssueDate.setText(date);
                    });
                }
            }
        });

        updateCertificateExpiryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.setCanceledOnTouchOutside(false);
                    datePickerDialog.show();
                    datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        updateCertificateExpiryDate.setText(date);
                    });
                }
            }
        });

        updateCertificateBtn.setOnClickListener(v -> updateCertificate(certificateId));
    }

    private void loadCertificateData(String certificateId) {
        db.collection("certificates").document(certificateId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Certificate certificate = documentSnapshot.toObject(Certificate.class);
                        if (certificate != null) {
                            updateCertificateId.setText(certificate.getId());
                            updateCertificateName.setText(certificate.getName());
                            updateCertificateCore.setText(certificate.getCores());
                            updateCertificateIssueDate.setText(certificate.getIssueDate());
                            updateCertificateExpiryDate.setText(certificate.getExpiryDate());
                        }
                    } else {
                        Toast.makeText(this, "Certificate not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading certificate data", Toast.LENGTH_SHORT).show());
    }

    private void updateCertificate(String certificateId) {
        String id = updateCertificateId.getText().toString();
        String studentId = getIntent().getStringExtra("studentId");
        String name = updateCertificateName.getText().toString();
        String cores = updateCertificateCore.getText().toString();
        String issueDate = updateCertificateIssueDate.getText().toString();
        String expiryDate = updateCertificateExpiryDate.getText().toString();

        Certificate certificate = new Certificate(id, name , studentId, cores, issueDate, expiryDate);

        db.collection("certificates").document(certificateId)
                .set(certificate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Certificate updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating certificate", Toast.LENGTH_SHORT).show());
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
