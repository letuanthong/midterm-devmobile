package dev.mobile.midterm.student;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import dev.mobile.midterm.R;

class StudentViewHolder extends RecyclerView.ViewHolder {
    TextView studentNameTxtView, studentMajorTxtView, studentIDTxtView, studentJoinedDateTxtView;
    ImageView updateBtn, deleteBtn;
    CardView studentCard;
    public StudentViewHolder(@NonNull View itemView) {
        super(itemView);
        studentNameTxtView = itemView.findViewById(R.id.studentNameTxtView);
        studentMajorTxtView = itemView.findViewById(R.id.studentMajorTxtView);
        studentIDTxtView = itemView.findViewById(R.id.studentID);
        studentJoinedDateTxtView = itemView.findViewById(R.id.studentJoinedDateTxtView);
        updateBtn = itemView.findViewById(R.id.updateStudentBtn);
        deleteBtn = itemView.findViewById(R.id.deleteStudentBtn);
        studentCard = itemView.findViewById(R.id.recyclerStudentCard);
    }
}
