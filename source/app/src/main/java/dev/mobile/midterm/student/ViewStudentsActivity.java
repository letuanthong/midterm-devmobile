package dev.mobile.midterm.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import dev.mobile.midterm.MainActivity;
import dev.mobile.midterm.R;
import dev.mobile.midterm.model.Student;

public class ViewStudentsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Student> studentList;
    FirebaseFirestore firestore;
    CollectionReference studentsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Student List");
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ViewStudentsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerStudentView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        studentList = new ArrayList<>();
        StudentAdapter adapter = new StudentAdapter(this, studentList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        studentsCollection = firestore.collection("students");

        studentsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ViewStudentsActivity.this, "Cannot get students", Toast.LENGTH_SHORT).show();
                    return;
                }

                studentList.clear();
                for (QueryDocumentSnapshot document : snapshots) {
                    Student student = document.toObject(Student.class);
                    studentList.add(student);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    //Sort vs Search Student
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_students, menu);

        // Configure SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            assert searchView != null;
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterStudents(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterStudents(newText);
                    return true;
                }
            });
        }

        return true;
    }

    private void filterStudents(String query) {
        List<Student> filteredList = new ArrayList<>();
        for (Student student : studentList) {
            if (student.getName().toLowerCase().contains(query.toLowerCase()) ||
                    student.getStudentId().toLowerCase().contains(query.toLowerCase()) ||
                    student.getJoiningDate().toLowerCase().contains(query.toLowerCase()) ||
                    student.getMajor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(student);
            }
        }
        StudentAdapter adapter = new StudentAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.action_sort));
            popupMenu.getMenu().add("Sort by Name");
            popupMenu.getMenu().add("Sort by Student ID");
            popupMenu.getMenu().add("Sort by Major");
            popupMenu.getMenu().add("Sort by Joining Date");

            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (Objects.requireNonNull(menuItem.getTitle()).toString()) {
                    case "Sort by Name":
                        studentList.sort(Comparator.comparing(Student::getName));
                        break;
                    case "Sort by Student ID":
                        studentList.sort(Comparator.comparing(Student::getStudentId));
                        break;
                    case "Sort by Major":
                        studentList.sort(Comparator.comparing(Student::getMajor));
                        break;
                    case "Sort by Joining Date":
                        studentList.sort(Comparator.comparing(Student::getJoiningDate));
                        break;
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                return true;
            });

            popupMenu.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}