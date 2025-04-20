package dev.mobile.midterm.student;

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
import dev.mobile.midterm.model.Student;

public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    private Context mContext;
    private List<Student> studentList;

    public StudentAdapter(Context mContext, List<Student> studentList) {
        this.mContext = mContext;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.studentNameTxtView.setText(student.getName());
        holder.studentIDTxtView.setText(student.getStudentId());
        holder.studentMajorTxtView.setText(student.getMajor());
        holder.studentJoinedDateTxtView.setText(student.getJoiningDate());
        holder.studentCard.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StudentDetailActivity.class);
            intent.putExtra("id", student.getId());
            intent.putExtra("studentId", student.getStudentId());
            intent.putExtra("name", student.getName());
            intent.putExtra("dateOfBirth", student.getDateOfBirth());
            intent.putExtra("phone", student.getPhone());
            intent.putExtra("status", student.getStatus());
            intent.putExtra("major", student.getMajor());
            intent.putExtra("classYear", student.getClassYear());
            intent.putExtra("joiningDate", student.getJoiningDate());
            intent.putExtra("gpa", student.getGpa());
            mContext.startActivity(intent);
        });

        holder.updateBtn.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, UpdateStudentActivity.class);
            intent.putExtra("id", student.getId());
            intent.putExtra("studentId", student.getStudentId());
            intent.putExtra("name", student.getName());
            intent.putExtra("dateOfBirth", student.getDateOfBirth());
            intent.putExtra("phone", student.getPhone());
            intent.putExtra("status", student.getStatus());
            intent.putExtra("major", student.getMajor());
            intent.putExtra("classYear", student.getClassYear());
            intent.putExtra("joiningDate", student.getJoiningDate());
            intent.putExtra("gpa", student.getGpa());
            mContext.startActivity(intent);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("students").document(student.getId())
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
        return studentList.size();
    }
}