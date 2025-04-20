package dev.mobile.midterm.certificate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Certificate;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateViewHolder> {
    private Context mContext;
    private List<Certificate> certificateList;

    public CertificateAdapter(Context mContext, List<Certificate> certificateList) {
        this.mContext = mContext;
        this.certificateList = certificateList;
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_certificate_item, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        Certificate certificate = certificateList.get(position);
        holder.certificateNameTxtView.setText(certificate.getName());
        holder.certificateCoreTxtView.setText(certificate.getCores());
        holder.issueDateTxtView.setText(certificate.getIssueDate());
        holder.expiryDateTxtView.setText(certificate.getExpiryDate());

        // Set click listeners for update and delete buttons
        holder.updateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UpdateCertificateActivity.class);
            intent.putExtra("id", certificate.getId());
            intent.putExtra("name", certificate.getName());
            intent.putExtra("cores", certificate.getCores());
            intent.putExtra("issueDate", certificate.getIssueDate());
            intent.putExtra("expiryDate", certificate.getExpiryDate());
            intent.putExtra("studentId", certificate.getStudentId());
            mContext.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete Certificate")
                    .setMessage("Are you sure you want to delete this certificate?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete the certificate from Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("certificates").document(certificate.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(mContext, "Error deleting student: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return certificateList.size();
    }
}
