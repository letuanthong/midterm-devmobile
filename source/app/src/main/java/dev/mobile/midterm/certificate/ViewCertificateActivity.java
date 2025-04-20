package dev.mobile.midterm.certificate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Certificate;

public class ViewCertificateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Certificate> certificateList;
    FirebaseFirestore firestore;
    CollectionReference certificatesCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certificates);

        recyclerView = findViewById(R.id.recyclerCertificateView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        certificateList = new ArrayList<>();
        CertificateAdapter adapter = new CertificateAdapter(this, certificateList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        certificatesCollection = firestore.collection("certificates");

        // Get the studentId passed from the previous activity
        String studentId = getIntent().getStringExtra("studentId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(studentId);
        }

        // Query certificates where studentId matches
        certificatesCollection.whereEqualTo("studentId", studentId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(ViewCertificateActivity.this, "Cannot get certificate", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        certificateList.clear();
                        for (QueryDocumentSnapshot document : snapshots) {
                            Certificate certificate = document.toObject(Certificate.class);
                            certificateList.add(certificate);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_certificate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_certificate) {
            // Handle the "+" button click
            Intent intent = new Intent(this, AddCertificateActivity.class);
            intent.putExtra("studentId", getIntent().getStringExtra("studentId"));
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
