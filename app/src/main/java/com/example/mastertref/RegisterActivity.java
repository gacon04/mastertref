package com.example.mastertref;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mastertref.database.IDGenerator;
import com.example.mastertref.database.TaikhoanDAO;
import com.example.mastertref.models.Taikhoan;

public class RegisterActivity extends AppCompatActivity {
    public boolean isValidPassword(String password) {
        // Regex: Ít nhất 8 ký tự, có chữ cái, chữ số và ký tự đặc biệt
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TaikhoanDAO taikhoanDAO = new TaikhoanDAO(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc activity hiện tại để quay lại activity đang onPause trước đó
                finish();
            }
        });

        // xử lý khi bấm nút đăng ký
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            EditText edtName = findViewById(R.id.edtFullName);
            EditText edtEmail = findViewById(R.id.edtEmail);
            EditText edtPassword = findViewById(R.id.edtPassword);
            EditText edtConfPass = findViewById(R.id.edtConfirmPassword);


            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confPass = edtConfPass.getText().toString().trim();
            String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if (isValidPassword(password))
            {
                Toast.makeText(this, "Mật khẩu phải chứa ít nhất 8 ký tự, có chữ cái, chữ số và ký tự đặc biệt", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confPass)) {
                Toast.makeText(this, "Vui lòng nhập mật khẩu trùng nhau", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.matches(emailPattern)) {
                Toast.makeText(this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || confPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            taikhoanDAO.open();
            boolean checkReg = taikhoanDAO.isEmailExists(email);
            if (checkReg) {
                Toast.makeText(this, "Email đã tồn tại trong hệ thống!", Toast.LENGTH_SHORT).show();
                taikhoanDAO.close();
                return;
            }
            else
            {

                Taikhoan newAccount = new Taikhoan(
                        IDGenerator.generateTrefID(),
                        password,
                        email,
                        2, // Role 1 là admin, role 2 là user
                        name,
                        "https://res.cloudinary.com/dmrexfwzv/image/upload/v1741879798/ic_launcher-playstore_pmtrat.png", // Avatar mặc định
                        null,
                        null,
                        1 // isActive
                );
                long result = taikhoanDAO.insertTaikhoan(newAccount);

                taikhoanDAO.close();
                if (result != -1) {
                    Toast.makeText(this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình chính
                    finish();
                } else {
                    Toast.makeText(this, "Đăng ký tài khoản thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}