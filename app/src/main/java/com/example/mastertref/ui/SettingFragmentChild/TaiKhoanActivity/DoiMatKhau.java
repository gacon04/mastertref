package com.example.mastertref.ui.SettingFragmentChild.TaiKhoanActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.TaikhoanDAO;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.AESHelper;

public class DoiMatKhau extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword;
    private Button btnChangePassword;
    private TaikhoanDAO taikhoanDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        // Ánh xạ View
        edtOldPassword = findViewById(R.id.edtCurrentPass);
        edtNewPassword = findViewById(R.id.edtNewPass);
        btnChangePassword = findViewById(R.id.btn_send);

        // Lấy DAO từ Database
        AppDatabase db = AppDatabase.getInstance(this);
        taikhoanDAO = db.taikhoanDAO();

        // Xử lý sự kiện đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        int userId = getUserIdFromSession(); // Lấy userId từ session hoặc intent
        String oldPassword = edtOldPassword.getText().toString().trim();
        String newPassword = edtNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mã hóa mật khẩu cũ
        String encryptedOldPassword = AESHelper.encrypt(oldPassword);
        TaikhoanEntity user = taikhoanDAO.verifyPassword(userId, encryptedOldPassword);

        if (user != null) {
            // Mật khẩu đúng → Cập nhật mật khẩu mới
            String encryptedNewPassword = AESHelper.encrypt(newPassword);
            taikhoanDAO.updatePassword(userId, encryptedNewPassword);
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        } else {
            // Mật khẩu cũ sai
            Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    private int getUserIdFromSession() {
        // TODO: Lấy userId từ session, SharedPreferences hoặc Intent
        return 1; // Ví dụ userId = 1

    }
}