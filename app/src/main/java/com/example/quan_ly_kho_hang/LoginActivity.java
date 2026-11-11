package com.example.quan_ly_kho_hang;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quan_ly_kho_hang.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvErrorMessage;
    private MaterialButton btnLogin;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tvErrorMessage.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            tvErrorMessage.setVisibility(View.VISIBLE);
            return;
        }

        boolean isValid = dbHelper.checkLogin(username, password);

        if (isValid) {
            tvErrorMessage.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            tvErrorMessage.setText("Sai tên đăng nhập hoặc mật khẩu");
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }
}
