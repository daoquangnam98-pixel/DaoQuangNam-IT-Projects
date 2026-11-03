package com.example.demoappbanhang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);

        btnLogin.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Kiểm tra tài khoản trong Room
            User user = UserDatabase
                    .getInstance(this)
                    .userDao()
                    .login(email, password);

            if (user != null) {

                // Lưu trạng thái đăng nhập
                SharedPreferences preferences =
                        getSharedPreferences("UserPrefs", MODE_PRIVATE);

                preferences.edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("userName", user.getName())
                        .putString("userEmail", user.getEmail())
                        .apply();

                Toast.makeText(
                        this,
                        "Đăng nhập thành công!",
                        Toast.LENGTH_SHORT
                ).show();

                // Chuyển sang trang chính
                Intent intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();

            } else {

                Toast.makeText(
                        this,
                        "Email hoặc mật khẩu không đúng!",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Chuyển sang đăng ký
        txtRegister.setOnClickListener(v -> {

            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );

            startActivity(intent);
        });
    }
}