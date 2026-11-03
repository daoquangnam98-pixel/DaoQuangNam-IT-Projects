package com.example.demoappbanhang;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    Button btnRegister;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        btnRegister.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword =
                    edtConfirmPassword.getText().toString().trim();

            // Kiểm tra nhập đủ
            if (name.isEmpty() ||
                    email.isEmpty() ||
                    password.isEmpty() ||
                    confirmPassword.isEmpty()) {

                Toast.makeText(
                        this,
                        "Vui lòng nhập đầy đủ thông tin",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Kiểm tra mật khẩu
            if (!password.equals(confirmPassword)) {

                Toast.makeText(
                        this,
                        "Mật khẩu nhập lại không khớp",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Kiểm tra email đã tồn tại
            UserDatabase database = UserDatabase.getInstance(this);

            User oldUser = database
                    .userDao()
                    .getUserByEmail(email);

            if (oldUser != null) {

                Toast.makeText(
                        this,
                        "Email này đã được đăng ký!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Tạo tài khoản
            User user = new User(
                    name,
                    email,
                    password
            );

            database.userDao().insert(user);

            Toast.makeText(
                    this,
                    "Đăng ký thành công!",
                    Toast.LENGTH_SHORT
            ).show();

            // Chuyển sang Login
            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });

        // Nút quay lại đăng nhập
        txtLogin.setOnClickListener(v -> {

            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });
    }
}