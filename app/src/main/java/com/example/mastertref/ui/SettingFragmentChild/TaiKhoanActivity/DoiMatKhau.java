package com.example.mastertref.ui.SettingFragmentChild.TaiKhoanActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.domain.models.ChangePasswordDTO;
import com.example.mastertref.domain.usecase.ChangePasswordUC;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class DoiMatKhau extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPass;
    private Button btnChangePassword;
    private ImageView btnBack;
    private SessionManager sessionManager;
    private ChangePasswordUC changePasswordUC;
    private TaikhoanVM taikhoanVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        // Khởi tạo các thành phần
        initComponents();
        // Thiết lập các listener
        setupListeners();
    }

    private void initComponents() {
        // Ánh xạ View
        edtOldPassword = findViewById(R.id.edtCurrentPass);
        edtNewPassword = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btn_send);
        btnBack = findViewById(R.id.back_button);

        // Khởi tạo ViewModel và các thành phần khác
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        sessionManager = new SessionManager(this);
        changePasswordUC = new ChangePasswordUC(taikhoanVM.getTaikhoanDAO());
    }

    private void setupListeners() {
        // Xử lý nút back
        btnBack.setOnClickListener(v -> finish());

        // Thêm TextWatcher để kiểm tra dữ liệu khi người dùng nhập
        edtOldPassword.addTextChangedListener(passwordWatcher);
        edtNewPassword.addTextChangedListener(passwordWatcher);
        edtConfirmPass.addTextChangedListener(passwordWatcher);

        // Xử lý sự kiện đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateInput();
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void validateInput() {
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        boolean isValid = true;

        if (oldPassword.isEmpty()) {
            edtOldPassword.setError("Vui lòng nhập mật khẩu cũ!");
            isValid = false;
        } else {
            edtOldPassword.setError(null);
        }

        if (newPassword.isEmpty()) {
            edtNewPassword.setError("Vui lòng nhập mật khẩu mới!");
            isValid = false;
        } else if (!isValidPassword(newPassword)) {
            edtNewPassword.setError("Mật khẩu ít nhất 8 ký tự, có chữ cái, số, ký tự đặc biệt!");
            isValid = false;
        } else if (newPassword.equals(oldPassword)) {
            edtNewPassword.setError("Mật khẩu mới không được giống mật khẩu cũ!");
            isValid = false;
        }
        else {
            edtNewPassword.setError(null);
        }

        if (confirmPassword.isEmpty()) {
            edtConfirmPass.setError("Vui lòng xác nhận mật khẩu mới!");
            isValid = false;
        } else if (!newPassword.equals(confirmPassword)) {
            edtConfirmPass.setError("Mật khẩu xác nhận không khớp!");
            isValid = false;
        } else {
            edtConfirmPass.setError(null);
        }

        btnChangePassword.setEnabled(isValid);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    private void changePassword() {
        String username = sessionManager.getUsername();
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPass.getText().toString().trim();

        ChangePasswordDTO dto = new ChangePasswordDTO(username, oldPassword, newPassword, confirmPassword);

        new Thread(() -> {
            changePasswordUC.execute(dto, new ChangePasswordUC.OnChangePasswordListener() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> {
                        Toast.makeText(DoiMatKhau.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> Toast.makeText(DoiMatKhau.this, message, Toast.LENGTH_SHORT).show());
                }
            });
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}