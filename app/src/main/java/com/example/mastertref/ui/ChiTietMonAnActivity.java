package com.example.mastertref.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChiTietMonAnActivity extends AppCompatActivity {
    private MonAnVM viewModel;
    private int monAnId;
    private SessionManager sessionManager;
    private String username;
    // UI elements
    private ImageView imgRecipe, imgAuthor, imgAuthorAvatar;
    private TextView tvRecipeTitle, tvAuthorName, tvAuthorUsername, tvDescription, tvAuthor2;
    private TextView tvRecipeId, tvPublishDate, tvUpdateDate, tvSoNguoi;
    private LinearLayout listNguyenLieu, listCachLam, llShowKhauPhan;
    private ImageButton btnBack, btnSave, btnMore, btnBookmark;
    private Button btnAddFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_mon);

        // Get recipe ID from intent
        monAnId = getIntent().getIntExtra("mon_an_id", -1);
        if (monAnId == -1) {
            Toast.makeText(this, "Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MonAnVM.class);
        sessionManager = new SessionManager(this);
        username = sessionManager.getUsername();
        // Initialize UI elements
        initViews();

        // Set click listeners
        setupClickListeners();

        // Load recipe data
        loadMonAnData();

    }

    private void initViews() {

        imgRecipe = findViewById(R.id.imgRecipe);
        imgAuthor = findViewById(R.id.imgAuthor);
        imgAuthorAvatar = findViewById(R.id.imgAuthorAvatar);
        tvSoNguoi = findViewById(R.id.tvSoNguoi);
        tvAuthor2 = findViewById(R.id.tvAuthor2);

        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvAuthorUsername = findViewById(R.id.tvAuthorUsername);
        tvDescription = findViewById(R.id.tvDescription);
        tvRecipeId = findViewById(R.id.tvRecipeId);
        tvPublishDate = findViewById(R.id.tvPublishDate);
        tvUpdateDate = findViewById(R.id.tvUpdateDate);
        listNguyenLieu = findViewById(R.id.listNguyenLieu);
        listCachLam = findViewById(R.id.listCachLam);
        llShowKhauPhan = findViewById(R.id.llShowKhauPhan);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        btnMore = findViewById(R.id.btnMore);
        btnAddFriend = findViewById(R.id.btnFollow);
        btnBookmark = findViewById(R.id.btnBookmark);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> {
            // Implement save functionality
            Toast.makeText(this, "Đã lưu công thức", Toast.LENGTH_SHORT).show();
        });

        btnMore.setOnClickListener(v -> {
            // Implement more options menu
            Toast.makeText(this, "Thêm tùy chọn", Toast.LENGTH_SHORT).show();
        });

        btnBookmark.setOnClickListener(v -> {
            // Implement bookmark functionality
            Toast.makeText(this, "Đã đánh dấu", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadMonAnData() {
        viewModel.getMonAnWithChiTietById(monAnId).observe(this, monAnWithChiTiet -> {
            if (monAnWithChiTiet != null) {
                displayMonAnData(monAnWithChiTiet);
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayMonAnData(MonAnWithChiTiet monAnWithChiTiet) {
        MonAnEntity monAn = monAnWithChiTiet.getMonAn();
        List<NguyenLieuEntity> nguyenLieuList = monAnWithChiTiet.getNguyenLieuList();
        List<BuocNauEntity> buocNauList = monAnWithChiTiet.getBuocNauList();

        // Set basic recipe info
        tvRecipeTitle.setText(monAn.getTenMonAn());
        tvDescription.setText(monAn.getMoTa());

        // Load image using Glide
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            Glide.with(this)
                    .load(monAn.getHinhAnh())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(imgRecipe);
        }

        if (monAnWithChiTiet.getNguoiDang().getAvatar() != null && !monAnWithChiTiet.getNguoiDang().getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(monAnWithChiTiet.getNguoiDang().getAvatar())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(imgAuthor);

        }
        if (monAnWithChiTiet.getNguoiDang().getAvatar() != null && !monAnWithChiTiet.getNguoiDang().getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(monAnWithChiTiet.getNguoiDang().getAvatar())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(imgAuthorAvatar);

        }
        if (monAn.getKhauPhan() != null && !monAn.getKhauPhan().isEmpty()) {
            tvSoNguoi.setText(""+monAn.getKhauPhan());
        } else {
            llShowKhauPhan.setVisibility(View.GONE);
        }
        // Set recipe metadata
        tvRecipeId.setText("ID Công thức: " + monAn.getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("d 'tháng' M, yyyy", Locale.getDefault());
        String createDate = dateFormat.format(new Date(monAn.getCreateAt()));
        String updateDate = dateFormat.format(new Date(monAn.getUpdateAt()));

        tvPublishDate.setText("Lên sóng vào " + createDate);
        if (updateDate.equals(createDate)) {
            tvUpdateDate.setVisibility(View.GONE);
        } else {
            tvUpdateDate.setVisibility(View.VISIBLE);
        }
        tvUpdateDate.setText("Cập nhật vào " + updateDate);

        // Set author info (you might need to fetch this from TaiKhoanEntity)
        // For now using placeholder
        tvAuthorName.setText("" + monAnWithChiTiet.getNguoiDang().getFullname());
        tvAuthor2.setText("" + monAnWithChiTiet.getNguoiDang().getFullname());
        tvAuthorUsername.setText(" @"+monAnWithChiTiet.getNguoiDang().getUsername());


        if (username.equals(monAnWithChiTiet.getNguoiDang().getUsername()))
        {
            btnSave.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            btnBookmark.setVisibility(View.GONE);
            btnAddFriend.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
            btnMore.setVisibility(View.VISIBLE);
            btnBookmark.setVisibility(View.VISIBLE);
            btnAddFriend.setVisibility(View.VISIBLE);
        }

        // Display ingredients
        displayIngredients(nguyenLieuList);

        // Display cooking steps
        displayCookingSteps(buocNauList);
    }

    private void displayIngredients(List<NguyenLieuEntity> nguyenLieuList) {
        // Clear previous ingredients
        listNguyenLieu.removeAllViews();

        for (NguyenLieuEntity nguyenLieu : nguyenLieuList) {
            View ingredientView = getLayoutInflater().inflate(
                    R.layout.item_nguyenlieu, listNguyenLieu, false);

            TextView tvQuantity = ingredientView.findViewById(R.id.tvQuantity);
            TextView tvIngredientName = ingredientView.findViewById(R.id.tvIngredientName);

            tvQuantity.setText(nguyenLieu.getDinhLuong() +" " );
            tvIngredientName.setText(nguyenLieu.getTenNguyenLieu());

            listNguyenLieu.addView(ingredientView);
        }
    }

    private void displayCookingSteps(List<BuocNauEntity> buocNauList) {
        // Clear previous steps
        listCachLam.removeAllViews();

        // Sort steps by step number
        buocNauList.sort((step1, step2) ->
                Integer.compare(step1.getSoBuoc(), step2.getSoBuoc()));

        for (int i = 0; i < buocNauList.size(); i++) {
            BuocNauEntity buocNau = buocNauList.get(i);

            View stepView = getLayoutInflater().inflate(
                    R.layout.item_buocnau, listCachLam, false);

            TextView tvStepNumber = stepView.findViewById(R.id.tvStepNumber);
            TextView tvStepDescription = stepView.findViewById(R.id.tvStepDescription);

            tvStepNumber.setText(String.valueOf(i + 1));
            tvStepDescription.setText(buocNau.getMoTa());

            listCachLam.addView(stepView);
        }
    }
}

