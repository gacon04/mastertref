package com.example.mastertref.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class LoginActivity extends AppCompatActivity {
    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    // Khai báo EditText
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView tvRegister, tvQuenmatkhau;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        taikhoanVM.createDefaultUserIfNotExists();
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        // Gán view chỉ 1 lần
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvQuenmatkhau = findViewById(R.id.tv_forgot_pass);

        // Xử lý điều hướng
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        tvQuenmatkhau.setOnClickListener(v -> startActivity(new Intent(this, ForgotPassword.class)));

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });

    }

    // Hàm xử lý đăng nhập riêng biệt
    private void checkLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        taikhoanVM.validLoginInfo(email, password, new TaikhoanVM.OnLoginResultListener() {
            @Override
            public void onResult(TaikhoanEntity user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            saveLoginSession(user.getUsername());
                        } else {
                            Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void saveLoginSession(String username) {
        sessionManager.saveUserSession(username);
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Đóng LoginActivity
    }

}

