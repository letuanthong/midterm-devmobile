package dev.mobile.midterm.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

import dev.mobile.midterm.MainActivity;
import dev.mobile.midterm.R;
import dev.mobile.midterm.model.User;

public class ViewUsersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<User> userList;
    FirebaseFirestore firestore;
    CollectionReference usersCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("User List");
        }

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ViewUsersActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        UserAdapter adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        usersCollection = firestore.collection("users");

        usersCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ViewUsersActivity.this, "Cannot get users", Toast.LENGTH_SHORT).show();
                    return;
                }

                userList.clear();
                for (QueryDocumentSnapshot document : snapshots) {
                    User user = document.toObject(User.class);
                    user.setId(document.getId()); // Set the document ID as the user ID
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}