package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.AESHelper;
import com.example.mastertref.utils.IDGenerator;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class RegisterActivity extends AppCompatActivity {
    private TaikhoanVM taikhoanVM;
    private EditText edtName, edtEmail, edtPassword, edtConfPass;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);

        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);

        // Ánh xạ view
        edtName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfPass = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // Quay lại màn hình login
        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(v -> finish());

        // Thêm TextWatcher để kiểm tra dữ liệu khi người dùng nhập
        edtEmail.addTextChangedListener(inputWatcher);
        edtPassword.addTextChangedListener(inputWatcher);
        edtConfPass.addTextChangedListener(inputWatcher);
        edtName.addTextChangedListener(inputWatcher);

        // Xử lý đăng ký khi bấm nút
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    // TextWatcher kiểm tra dữ liệu nhập vào realtime
    private final TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validateInput();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    };

    private void validateInput() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confPass = edtConfPass.getText().toString().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập họ và tên!");
            isValid = false;
        } else if (name.split("\\s+").length < 2) {
            edtName.setError("Tên phải có ít nhất 2 từ!");
            isValid = false;
        } else {
            edtName.setError(null);
        }

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email!");
            isValid = false;
        }
        else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            edtEmail.setError("Email không hợp lệ!");
            isValid = false;
        } else {
            edtEmail.setError(null);
        }

        if (!isValidPassword(password)) {
            edtPassword.setError("Mật khẩu ít nhất 8 ký tự, có chữ cái, số, ký tự đặc biệt!");
            isValid = false;
        } else {
            edtPassword.setError(null);
        }

        if (!password.equals(confPass)) {
            edtConfPass.setError("Mật khẩu nhập lại không khớp!");
            isValid = false;
        } else {
            edtConfPass.setError(null);
        }

        // Chỉ bật nút Đăng ký nếu tất cả thông tin hợp lệ
        btnRegister.setEnabled(isValid);
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confPass = edtConfPass.getText().toString().trim();

        new Thread(() -> {
            if (taikhoanVM.isEmailExists(email)) {
                runOnUiThread(() -> Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show());
            } else {
                TaikhoanEntity newUser = new TaikhoanEntity(
                        IDGenerator.generateTrefID(), email, AESHelper.encrypt(password), name,"",
                        "", "https://res.cloudinary.com/dmrexfwzv/image/upload/ic_launcher-playstore_pmtrat.png", true);

                taikhoanVM.insertUser(newUser);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                });
            }
        }).start();
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }
}
