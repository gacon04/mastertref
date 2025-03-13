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

import com.example.mastertref.database.TaikhoanDAO;
import com.example.mastertref.models.Taikhoan;

public class LoginActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        // Khởi tao tài khoan dau tien trong he thong
        TaikhoanDAO taikhoanDAO = new TaikhoanDAO(this);
        taikhoanDAO.open();

        // ** Kiểm tra xem tài khoản "ngocminh04" đã tồn tại chưa**
        Taikhoan existingAccount = taikhoanDAO.getTaikhoanByUsername("ngocminh04");

        if (existingAccount == null) {
            // ** Chưa có tài khoản => Thêm mới**
            Taikhoan newAccount = new Taikhoan(
                    "ngocminh04",
                    "ngocminh04.",
                    "crushzone610@gmail.com",
                    2, // Role (Tùy chọn)
                    "Tran Ngoc Minh",
                    "default_avatar.png", // Avatar mặc định
                    "Vietnam",
                    "Tui yêu VN",
                    1 // isActive
            );

            long result = taikhoanDAO.insertTaikhoan(newAccount);
            if (result != -1) {
                Log.d("DATABASE", "Thêm tài khoản ngocminh04 thành công!");
            } else {
                Log.e("DATABASE", "Lỗi khi thêm tài khoản ngocminh04!");
            }
        } else {
            Log.d("DATABASE", "Tài khoản ngocminh04 đã tồn tại, không thêm mới.");
        }

        taikhoanDAO.close();

        // điều hướng cho nút đăng kí
        TextView tvRegister = findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // điều hướng cho nút quên mật khẩu
        TextView tvQuenmatkhau = findViewById(R.id.tv_forgot_pass);
        tvQuenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        // check tài khoản
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {

            EditText edtEmail = findViewById(R.id.edtEmail);
            EditText edtPassword = findViewById(R.id.edtPassword);

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ email và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            taikhoanDAO.open();
            boolean isLoginSuccess = taikhoanDAO.validLoginInfo(email, password);
            taikhoanDAO.close();
            if (isLoginSuccess) {
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                // Chuyển sang màn hình chính
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
            }
        });



    }

}
