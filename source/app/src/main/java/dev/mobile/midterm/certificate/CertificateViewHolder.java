package dev.mobile.midterm.certificate;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dev.mobile.midterm.R;

public class CertificateViewHolder extends RecyclerView.ViewHolder {
    TextView certificateNameTxtView, certificateCoreTxtView, issueDateTxtView, expiryDateTxtView;
    ImageView updateBtn, deleteBtn;
    CardView certificateCard;
    public CertificateViewHolder(@NonNull View itemView) {
        super(itemView);
        certificateNameTxtView = itemView.findViewById(R.id.certificateNameTxtView);
        certificateCoreTxtView = itemView.findViewById(R.id.certificateCoreTxtView);
        issueDateTxtView = itemView.findViewById(R.id.issueDateTxtView);
        expiryDateTxtView = itemView.findViewById(R.id.expiryDateTxtView);
        updateBtn = itemView.findViewById(R.id.updateCertificateBtn);
        deleteBtn = itemView.findViewById(R.id.deleteCertificateBtn);
        certificateCard = itemView.findViewById(R.id.recyclerCertificateCard);
    }
}
