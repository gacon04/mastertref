package com.example.mastertref.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.adapter.BinhLuanAdapter;
import com.example.mastertref.data.local.BinhLuanTaiKhoanEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.BinhLuanVM;
import com.example.mastertref.viewmodel.TaikhoanVM;

import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private ImageButton btnBack, btnCamera;
    private int monAnId;
    private SessionManager sessionManager;
    private String username;
    private TextView btnSend;
    private RecyclerView rvBinhLuans;
    private LinearLayout llEmptyBinhLuan;
    private EditText etComment;
    private BinhLuanVM binhLuanVM;
    private TaikhoanVM taiKhoanVM;

    private BinhLuanAdapter binhLuanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);

        try {
            // Khởi tạo ViewModel trước
            binhLuanVM = new ViewModelProvider(this).get(BinhLuanVM.class);
            taiKhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);

            // Kiểm tra Intent
            if (getIntent() == null || !getIntent().hasExtra("mon_an_id")) {
                Toast.makeText(this, "Lỗi khi mở màn hình bình luận", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            monAnId = getIntent().getIntExtra("mon_an_id", -1);
            if (monAnId == -1) {
                Toast.makeText(this, "Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Khởi tạo SessionManager và lấy username
            sessionManager = new SessionManager(this);
            username = sessionManager.getUsername();
            if (username == null || username.isEmpty()) {
                Toast.makeText(this, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            initViews();
            setupAdapter();
            setupListeners();
            loadBinhLuans();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khởi tạo màn hình bình luận: " + e.getMessage(), 
                Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            finish();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.backButton);
        btnCamera = findViewById(R.id.cameraButton);
        btnSend = findViewById(R.id.sendButton);
        rvBinhLuans = findViewById(R.id.commentsRecyclerView);
        llEmptyBinhLuan = findViewById(R.id.emptyComment);
        etComment = findViewById(R.id.commentInput);
    }

    private void setupAdapter() {
        binhLuanAdapter = new BinhLuanAdapter(this);
        rvBinhLuans.setAdapter(binhLuanAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            String commentText = etComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                try {
                    taiKhoanVM.getUserByUsername(username).observe(this, user -> {
                        if (user != null) {
                            int userId = user.getId();
                            binhLuanVM.insertBinhLuan(userId, monAnId, commentText, null);
                            etComment.setText("");
                            Toast.makeText(CommentActivity.this, "Đã gửi bình luận", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CommentActivity.this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(CommentActivity.this, "Lỗi khi gửi bình luận", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Vô hiệu hóa nút gửi khi không có nội dung
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    btnSend.setTextColor(getResources().getColor(R.color.gray));
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setTextColor(getResources().getColor(R.color.dark_orange));
                    btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý nút camera (chức năng thêm hình ảnh)
        btnCamera.setOnClickListener(v -> {
            // Thêm chức năng chọn ảnh từ thư viện hoặc chụp ảnh
            Toast.makeText(CommentActivity.this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadBinhLuans() {
        try {
            binhLuanVM.getBinhLuansWithUserByMonId(monAnId).observe(this, binhLuans -> {
                if (binhLuans != null && !binhLuans.isEmpty()) {
                    updateBinhLuanUI(binhLuans);
                } else {
                    showEmptyState();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tải bình luận", Toast.LENGTH_SHORT).show();
            showEmptyState();
        }
    }

    private void updateBinhLuanUI(List<BinhLuanTaiKhoanEntity> binhLuans) {
        binhLuanAdapter.setBinhLuans(binhLuans);
        rvBinhLuans.setVisibility(View.VISIBLE);
        llEmptyBinhLuan.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        rvBinhLuans.setVisibility(View.GONE);
        llEmptyBinhLuan.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            finish();
        }
    }
}