package dev.mobile.midterm.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dev.mobile.midterm.R;

public class UserDetailActivity extends AppCompatActivity {
    TextView  userTitle, userDetailId, userDetailName, userDetailEmail, userDetailPhoneNumber, userDetailStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userTitle = findViewById(R.id.userTitle);
        userDetailId = findViewById(R.id.userDetailId);
        userDetailName = findViewById(R.id.userDetailName);
        userDetailEmail = findViewById(R.id.userDetailEmail);
        userDetailPhoneNumber = findViewById(R.id.userDetailPhone);
        userDetailStatus = findViewById(R.id.userDetailStatus);

        Intent intent = getIntent();
        userDetailId.setText(intent.getStringExtra("Key"));
        userDetailName.setText(intent.getStringExtra("Name"));
        userTitle.setText(intent.getStringExtra("Name"));
        userDetailEmail.setText(intent.getStringExtra("Email"));
        userDetailPhoneNumber.setText(intent.getStringExtra("Phone"));
        userDetailStatus.setText(intent.getStringExtra("Status"));
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
