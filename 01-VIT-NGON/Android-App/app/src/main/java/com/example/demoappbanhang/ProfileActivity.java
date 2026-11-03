package com.example.demoappbanhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView txtUserName;
    TextView txtUserEmail;
    TextView txtInfoName;
    TextView txtInfoEmail;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtInfoName = findViewById(R.id.txtInfoName);
        txtInfoEmail = findViewById(R.id.txtInfoEmail);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences preferences =
                getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String userName =
                preferences.getString("userName", "Người dùng");

        String userEmail =
                preferences.getString("userEmail", "Chưa có email");

        txtUserName.setText(userName);
        txtUserEmail.setText(userEmail);

        txtInfoName.setText(userName);
        txtInfoEmail.setText(userEmail);

        btnLogout.setOnClickListener(v -> {

            preferences.edit().clear().apply();

            Intent intent = new Intent(
                    ProfileActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);

            finishAffinity();
        });
    }
}