package com.example.mastertref.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class ChangeMyInfo extends AppCompatActivity {

    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;

    private TextView tvName, tvUsername;
    private EditText edtName, edtUsername, edtEmail, edtFrom, edtDescription;
    private Button btnUpdate;
    private ImageView profileImage;

    private TaikhoanEntity currentUser;
    private String currentUsername;
    private boolean isUsernameAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_info);

        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        sessionManager = new SessionManager(this);
        currentUsername = sessionManager.getUsername();

        initViews();
        setupListeners();
        observeUserData();
    }

    private void initViews() {
        tvName = findViewById(R.id.tv_name);
        tvUsername = findViewById(R.id.tv_username);
        edtName = findViewById(R.id.edt_name);
        edtUsername = findViewById(R.id.edt_username);
        edtEmail = findViewById(R.id.edt_email);
        edtFrom = findViewById(R.id.edt_from);
        edtDescription = findViewById(R.id.edt_description);
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void setupListeners() {
        edtName.addTextChangedListener(inputWatcher);
        edtEmail.addTextChangedListener(inputWatcher);
        edtUsername.addTextChangedListener(usernameWatcher);

        btnUpdate.setOnClickListener(v -> {
            if (currentUser != null) {
                String newName = edtName.getText().toString().trim();
                String newFrom = edtFrom.getText().toString().trim();
                String newDescription = edtDescription.getText().toString().trim();
                String newEmail = edtEmail.getText().toString().trim();
                String newUsername = edtUsername.getText().toString().trim();

                currentUser.setFullname(newName);
                currentUser.setFrom(newFrom);
                currentUser.setDescription(newDescription);
                currentUser.setEmail(newEmail);
                currentUser.setUsername(newUsername);

                new Thread(() -> {
                    taikhoanVM.updateUser(currentUser);
                    sessionManager.saveUserSession(newUsername);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        currentUsername = newUsername;
                        taikhoanVM.getUserByUsername(currentUsername).removeObservers(this);
                        observeUserData();
                    });
                }).start();
            }
        });
    }

    private void observeUserData() {
        taikhoanVM.getUserByUsername(currentUsername).observe(this, user -> {
            if (user != null) {
                currentUser = user;
                updateUI(user);
            }
        });
    }

    private void updateUI(TaikhoanEntity user) {
        tvName.setText(user.getFullname());
        tvUsername.setText("@" + user.getUsername());
        edtName.setText(user.getFullname());
        edtUsername.setText(user.getUsername());
        edtEmail.setText(user.getEmail());
        edtFrom.setText(user.getFrom());
        edtDescription.setText(user.getDescription());
    }

    private final TextWatcher inputWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            validateInput();
        }
    };

    private final TextWatcher usernameWatcher = new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            String inputUsername = s.toString().trim();

            if (isValidUsernameFormat(inputUsername)) {
                taikhoanVM.getUserByUsername(inputUsername).observe(ChangeMyInfo.this, user -> {
                    if (user != null && !inputUsername.equals(currentUser.getUsername())) {
                        edtUsername.setError("Tên người dùng đã tồn tại!");
                        isUsernameAvailable = false;
                    } else {
                        edtUsername.setError(null);
                        isUsernameAvailable = true;
                    }
                    validateInput();
                });
            } else {
                isUsernameAvailable = false;
                validateInput();
            }
        }
    };

    private boolean isValidUsernameFormat(String username) {
        return username.matches("^[a-zA-Z][a-zA-Z0-9_]{1,}$");
    }

    private void validateInput() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String username = edtUsername.getText().toString().trim();

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
        } else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            edtEmail.setError("Email không hợp lệ!");
            isValid = false;
        } else {
            // Kiểm tra email trên background thread
            new Thread(() -> {
                boolean emailExists = taikhoanVM.isEmailExists(email);
                runOnUiThread(() -> {
                    if (emailExists && !email.equals(currentUser.getEmail())) {
                        edtEmail.setError("Email đã tồn tại!");
                        btnUpdate.setEnabled(false);
                    } else {
                        edtEmail.setError(null);
                        validateUsername(username);
                    }
                });
            }).start();
            return; // Tạm dừng validation cho đến khi kiểm tra email xong
        }

        validateUsername(username);
    }

    private void validateUsername(String username) {
        if (username.isEmpty()) {
            edtUsername.setError("Vui lòng nhập tên người dùng");
            btnUpdate.setEnabled(false);
        } else if (!isValidUsernameFormat(username)) {
            edtUsername.setError("Tên người dùng không hợp lệ (bắt đầu bằng chữ cái, ít nhất 2 ký tự)");
            btnUpdate.setEnabled(false);
        } else if (!isUsernameAvailable) {
            edtUsername.setError("Tên người dùng đã tồn tại!");
            btnUpdate.setEnabled(false);
        } else {
            edtUsername.setError(null);
            btnUpdate.setEnabled(true);
        }
    }
}
