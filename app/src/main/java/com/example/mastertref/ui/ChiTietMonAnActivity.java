package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.MonAnGoiYAdapter;
import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.DaXemGanDayVM;
import com.example.mastertref.viewmodel.MonAnVM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Add these imports at the top
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ChiTietMonAnActivity extends AppCompatActivity {
    private MonAnVM monAnVM;
    private TaikhoanVM taikhoanVM;
    private DaXemGanDayVM daXemRecentVM;
    private int currentUserId = -1, ownerId=-1;
    private int monAnId;
    private SessionManager sessionManager;
    private String username;
    // UI elements
    private ImageView imgRecipe, imgAuthor, imgAuthorAvatar, imgRecipeSmall;
    private TextView tvRecipeTitle, tvAuthorName, tvAuthorUsername, tvDescription, tvAuthor2, tvRecipeTitleSmall;
    private TextView tvRecipeId, tvPublishDate, tvUpdateDate, tvSoNguoi, tvShowTime, btnWriteComment;
    private LinearLayout listNguyenLieu, listCachLam, llShowKhauPhan, llShowTime, llAuthorBanner;
    private ImageButton btnBack, btnMore, btnBookmark;
    private Button btnAddFriend;
    private RecyclerView rvRecipeRecommendations;
    private MonAnGoiYAdapter recommendationsAdapter;

    private LinearLayout collapsedToolbarContent;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_mon);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        daXemRecentVM = new ViewModelProvider(this).get(DaXemGanDayVM.class);
        sessionManager = new SessionManager(this);
        initCurrentUserId();


        // Get recipe ID from intent
        monAnId = getIntent().getIntExtra("mon_an_id", -1);
        if (monAnId == -1) {
            Toast.makeText(this, "Không tìm thấy món ăn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Add this line to insert the recent view
        if (currentUserId > 0) {
            daXemRecentVM.saveUniqueRecentView(currentUserId, monAnId);
        } else {
            // If user ID is not available yet, set up a listener to wait for it
            taikhoanVM.getUserIdByUsername(username, userId -> {
                if (userId > 0) {
                    currentUserId = userId;
                    daXemRecentVM.saveUniqueRecentView(currentUserId, monAnId);
                }
            });
        }

        // Initialize ViewModel

        // Initialize UI elements
        initViews();

        // Set click listeners
        setupClickListeners();
        
        // Add this line to set up the AppBar scroll listener
        setupAppBarListener();

        // Load recipe data
        loadMonAnData();
        // Load recommended recipes
        loadRecommendedRecipes();

    }

    private void initViews() {

        imgRecipe = findViewById(R.id.imgRecipe);
        imgAuthor = findViewById(R.id.imgAuthor);
        imgAuthorAvatar = findViewById(R.id.imgAuthorAvatar);
        tvSoNguoi = findViewById(R.id.tvSoNguoi);
        tvAuthor2 = findViewById(R.id.tvAuthor2);
        tvShowTime = findViewById(R.id.tvShowTime);
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
        llAuthorBanner = findViewById(R.id.llAuthorBanner);
        llShowTime = findViewById(R.id.llShowTime);
        btnBack = findViewById(R.id.btnBack);
        btnMore = findViewById(R.id.btnMore);
        btnAddFriend = findViewById(R.id.btnFollow);
        btnBookmark = findViewById(R.id.btnBookmark);
        btnWriteComment = findViewById(R.id.btnWriteComment);
        // Load adapter de load len mon an goi y
        rvRecipeRecommendations = findViewById(R.id.rvRecipeRecommendations);
        recommendationsAdapter = new MonAnGoiYAdapter(this);
        rvRecipeRecommendations.setAdapter(recommendationsAdapter);

        tvRecipeTitleSmall = findViewById(R.id.tvRecipeTitleSmall);
        imgRecipeSmall = findViewById(R.id.imgRecipeSmall);
        
        // Add these lines to initialize the new UI elements
        collapsedToolbarContent = findViewById(R.id.collapsedToolbarContent);
        appBarLayout = findViewById(R.id.appBarLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
    }
    
    // Add this new method to handle the AppBar scroll behavior
    private void setupAppBarListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                
                // When collapsed (verticalOffset is negative, so we use abs)
                if (Math.abs(verticalOffset) > scrollRange / 2) {
                    if (!isShow) {
                        isShow = true;
                        collapsedToolbarContent.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (isShow) {
                        isShow = false;
                        collapsedToolbarContent.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            onBackPressed(); // Use onBackPressed instead of finish()
        });



        btnMore.setOnClickListener(v -> {
            // Implement more options menu
            Toast.makeText(this, "Thêm tùy chọn", Toast.LENGTH_SHORT).show();
        });

        btnBookmark.setOnClickListener(v -> {
            // Implement bookmark functionality
            Toast.makeText(this, "Đã đánh dấu", Toast.LENGTH_SHORT).show();
        });
        btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(ChiTietMonAnActivity.this, CommentActivity.class);
                    intent.putExtra("mon_an_id", monAnId);
                    // Thêm flag để giữ activity stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ChiTietMonAnActivity.this, 
                        "Lỗi khi mở màn hình bình luận", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
        llAuthorBanner.setOnClickListener(v -> {
            try {
                // Tạo intent để mở MainActivity
                Intent intent = new Intent(ChiTietMonAnActivity.this, MainActivity.class);
                // Thêm action đặc biệt để MainActivity biết cần mở fragment nào
                intent.setAction("OPEN_USER_PROFILE");
                // Truyền user_id của người tạo công thức
                intent.putExtra("user_id", ownerId);
                startActivity(intent);
                

            } catch (Exception e) {
                Toast.makeText(ChiTietMonAnActivity.this, 
                    "Lỗi khi mở trang cá nhân: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void loadMonAnData() {
        monAnVM.getMonAnWithChiTietById(monAnId).observe(this, monAnWithChiTiet -> {
            if (monAnWithChiTiet != null) {
                displayMonAnData(monAnWithChiTiet);
                ownerId = monAnWithChiTiet.getMonAn().getTaikhoanId();
            } else {
                Toast.makeText(this, "Không tìm thấy thông tin món ăn", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void loadRecommendedRecipes() {
        monAnVM.getRecommendedRecipes(monAnId, 8).observe(this, monAnList -> {
            if (monAnList != null && !monAnList.isEmpty()) {
                recommendationsAdapter.setData(monAnList);
            } else {
                // If no similar recipes found, load random recipes
                monAnVM.getRandomRecipesExcept(monAnId, 8).observe(this, randomList -> {
                    recommendationsAdapter.setData(randomList);
                });
            }
        });
    }
    private void initCurrentUserId() {
        username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
            });
        }
    }
    private void displayMonAnData(MonAnWithChiTiet monAnWithChiTiet) {
        MonAnEntity monAn = monAnWithChiTiet.getMonAn();
        List<NguyenLieuEntity> nguyenLieuList = monAnWithChiTiet.getNguyenLieuList();
        List<BuocNauEntity> buocNauList = monAnWithChiTiet.getBuocNauList();

        // Add this code to save the recently viewed item
        if (currentUserId > 0 && monAn.getTaikhoanId() != currentUserId) {
            // Only save to recently viewed if it's not the user's own recipe
            // This will ensure we're only tracking views of other users' recipes
            daXemRecentVM.saveUniqueRecentView(currentUserId, monAnId);
        }

        // Set basic recipe info
        tvRecipeTitle.setText(monAn.getTenMonAn());
        tvDescription.setText(monAn.getMoTa());
        tvRecipeTitleSmall.setText(monAn.getTenMonAn());

        // Load image using Glide
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            Glide.with(this)
                    .load(monAn.getHinhAnh())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(imgRecipe);
        }
        if (monAn.getHinhAnh() != null && !monAn.getHinhAnh().isEmpty()) {
            Glide.with(this)
                    .load(monAn.getHinhAnh())
                    .placeholder(R.drawable.mastertref)
                    .error(R.drawable.mastertref)
                    .into(imgRecipeSmall);
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
        if (monAn.getThoiGian()!= null && !monAn.getThoiGian().isEmpty()) {
            tvShowTime.setText(monAn.getThoiGian());
        } else {
            llShowTime.setVisibility(View.GONE);
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
            btnMore.setVisibility(View.GONE);
            btnBookmark.setVisibility(View.GONE);
            btnAddFriend.setVisibility(View.GONE);
        } else {
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
//        listNguyenLieu.removeAllViews();

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

    // Override onBackPressed to ensure consistent behavior
    @Override
    public void onBackPressed() {
        finish(); // Explicitly finish the activity
        // Don't call super.onBackPressed() to avoid potential double-back behavior
    }
}

