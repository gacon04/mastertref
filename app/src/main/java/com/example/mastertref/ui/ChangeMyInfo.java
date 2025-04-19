package com.example.mastertref.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.mastertref.R;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.CloudinaryHelper;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.MediaPermissionHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.app.AlertDialog;

import java.io.ByteArrayOutputStream;

public class ChangeMyInfo extends AppCompatActivity {

    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;

    private TextView tvName, tvUsername;
    private EditText edtName, edtUsername, edtEmail, edtFrom, edtDescription;
    private Button btnUpdate;
    private ImageView profileImage,  btnChangeImage;

    private TaikhoanEntity currentUser;
    private String currentUsername, uploadedImageUrl;
    private Uri selectedImageUri;
    private boolean isUsernameAvailable = true;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                selectedImageUri = result.getData().getData();
                // Hiển thị ảnh đã chọn
                profileImage.setImageURI(selectedImageUri);
                // Upload ảnh lên Cloudinary
                uploadImageToCloudinary();
            }
        }
    );

    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;

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
        btnChangeImage = findViewById(R.id.btn_changeAvatar);
        profileImage = findViewById(R.id.profile_image);
    }

    private void setupListeners() {
        edtName.addTextChangedListener(inputWatcher);
        edtEmail.addTextChangedListener(inputWatcher);
        edtUsername.addTextChangedListener(usernameWatcher);
        btnChangeImage.setOnClickListener(v -> {

            // Kiểm tra phiên bản Android và quyền tương ứng
            String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                    Manifest.permission.READ_MEDIA_IMAGES :
                    Manifest.permission.READ_EXTERNAL_STORAGE;

            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                // Nếu đã có quyền, hiển thị dialog chọn ảnh
                showImagePickerDialog();
            } else {
                // Nếu chưa có quyền, kiểm tra xem có nên hiển thị giải thích không
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Hiển thị dialog giải thích
                    new AlertDialog.Builder(this)
                            .setTitle("Cần cấp quyền")
                            .setMessage("Ứng dụng cần quyền truy cập ảnh để thay đổi ảnh đại diện")
                            .setPositiveButton("Đồng ý", (dialog, which) -> {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{permission},
                                        STORAGE_PERMISSION_CODE);
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                } else {
                    // Xin quyền trực tiếp
                    ActivityCompat.requestPermissions(this,
                            new String[]{permission},
                            STORAGE_PERMISSION_CODE);
                    showImagePickerDialog();
                }
            }
        });
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
                // Cập nhật imageLink nếu có
                if (uploadedImageUrl != null) {
                    currentUser.setImageLink(uploadedImageUrl);
                }

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
        
        // Load ảnh đại diện nếu có
        if (user.getImageLink() != null && !user.getImageLink().isEmpty()) {
            ImageHelper.loadImage(profileImage, user.getImageLink());
            profileImage.setBackground(Drawable.createFromPath("@drawable/circle_background"));
        }
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

    public void uploadImageToCloudinary() {
        if (selectedImageUri != null) {
            CloudinaryHelper.uploadImage(this, selectedImageUri, new CloudinaryHelper.CloudinaryCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    runOnUiThread(() -> {
                        uploadedImageUrl = imageUrl;
                        Toast.makeText(ChangeMyInfo.this,
                            "Upload ảnh thành công", Toast.LENGTH_SHORT).show();
                        // Cập nhật URL ảnh vào currentUser
                        if (currentUser != null) {
                            currentUser.setImageLink(uploadedImageUrl);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {

                        Toast.makeText(ChangeMyInfo.this,
                            "Lỗi upload ảnh: " + error, Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void showImagePickerDialog() {
        String[] options = {"Chụp ảnh mới", "Chọn từ thư viện"};
        new AlertDialog.Builder(this)
                .setTitle("Chọn ảnh từ")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Kiểm tra quyền camera
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.CAMERA},
                                    CAMERA_PERMISSION_CODE);
                            return;
                        }
                        // Mở camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);
                    } else {
                        // Mở gallery
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Khi được cấp quyền storage, hiển thị dialog chọn ảnh
                showImagePickerDialog();
            }
        } else if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Khi được cấp quyền camera, mở camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 1) { // Camera
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    selectedImageUri = getImageUri(this, imageBitmap);
                    profileImage.setImageURI(selectedImageUri);
                    uploadImageToCloudinary();
                }
            } else if (requestCode == 2) { // Gallery
                selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
                uploadImageToCloudinary();
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, "Title", null);
        return Uri.parse(path);
    }
}
