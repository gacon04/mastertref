package com.example.mastertref.ui;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastertref.utils.CloudinaryHelper;
import com.example.mastertref.utils.NguyenLieuUtils;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.databinding.ActivityAddRecipeBinding;
import com.example.mastertref.viewmodel.AddRecipeVM;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {
    private ActivityAddRecipeBinding binding;
    Button  btnAddMoreIngredient, btnAddMoreStep, btnAddRecipe;
    ImageButton btnClose;
    Button btnAddIngredient, btnAddStep, btnPost, btnDraft;
    LinearLayout ingredientsContainer, stepsContainer;
    EditText edtTitle, edtDescription, edtServing, edtCookingTime;
    ImageView imgAvatar,btnAddImage;
    FrameLayout layoutHinhAnh;
    LinearLayout pickAvatarImgLayout;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;

    String  uploadedImageUrl;
    private Uri selectedImageUri;
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    // Hiển thị ảnh đã chọn
                    imgAvatar.setImageURI(selectedImageUri);
                    // Upload ảnh lên Cloudinary
                    uploadImageToCloudinary();
                }
            }
    );
    private AddRecipeVM addRecipeVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Ánh xạ view
        btnClose = findViewById(R.id.btn_Close);
        btnAddStep = findViewById(R.id.btnAddStep);
        btnPost = findViewById(R.id.btnPost);
        btnAddImage = findViewById(R.id.btn_AddImage);
        btnDraft = findViewById(R.id.btnDraft);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        stepsContainer = findViewById(R.id.stepsContainer); // Tách ra dùng chung

        // Ánh xạ các view trong layout
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        edtServing = findViewById(R.id.edtServing);
        edtCookingTime = findViewById(R.id.edtCookingTime);
        imgAvatar = findViewById(R.id.imgRecipe);

        layoutHinhAnh = findViewById(R.id.addImageFL);
        addRecipeVM = new ViewModelProvider(this).get(AddRecipeVM.class);
        pickAvatarImgLayout = findViewById(R.id.pickAvatarImgLayout);
        // Khởi tạo các view và xử lý sự kiện
        initViews();
        setupListeners();

    // set sự kiện bấm nút Thêm Nguyên liệu thì tạo ra khối nguyên liệu
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu chưa có nguyên liệu nào thì thêm khối đầu tiên trước
                    addIngredientBlock();
            }
        });

    // set sự kiện bấm nút Thêm Bước thì tạo ra khối bước
        btnAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addStepBlock();
            }
        });
        layoutHinhAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageBlock();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRecipe();
            }
        });

        btnClose.setOnClickListener(v -> {
            finish();
        });

    }
    // hàm thêm khối nguyên liệu vào ingredientsContainer
    public void addIngredientBlock() {
        // Tạo LinearLayout chứa ingredient
        LinearLayout ingredientLayout = new LinearLayout(AddRecipeActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(40)
        );
        layoutParams.setMargins(dpToPx(4), 0, dpToPx(4), dpToPx(10));
        ingredientLayout.setLayoutParams(layoutParams);
        ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
        ingredientLayout.setGravity(Gravity.CENTER_VERTICAL);
        ingredientLayout.setElevation(dpToPx(2));

        // Tạo EditText
        EditText editText = new EditText(AddRecipeActivity.this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1
        );

        editText.setLayoutParams(editParams);
        editText.setPadding(dpToPx(10), 0, 0, 0);
        editText.setBackgroundResource(R.color.light_gray);
        editText.setTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.black));
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        editText.setTypeface(ResourcesCompat.getFont(AddRecipeActivity.this, R.font.sfpro_regu));
        editText.setHint("Nhập nguyên liệu và định lượng nhé");
        editText.setHintTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.gray));

        // Xử lý sự kiện khi người dùng nhập vào EditText
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                String[] words = text.split("\\s+");
                boolean hasNumberWord = false;

                for (String word : words) {
                    if (word.matches("\\d+")) {
                        hasNumberWord = true;
                        break;
                    }
                }

                if (!text.isEmpty() && !hasNumberWord) {
                    editText.setError("Chưa có số lượng");
                } else {
                    editText.setError(null);
                }
            }
        });

        // Tạo ImageView
        ImageView imageView = new ImageView(AddRecipeActivity.this);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                dpToPx(20), dpToPx(20)
        );
        imageParams.setMarginStart(dpToPx(8));
        imageView.setLayoutParams(imageParams);
        imageView.setImageResource(R.drawable.ic_delete);

        // Xử lý sự kiện xóa nguyên liệu (nếu còn từ 2 khối trở lên)
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientsContainer.getChildCount() > 1) {
                    ingredientsContainer.removeView(ingredientLayout);
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Cần ít nhất 1 nguyên liệu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thêm EditText và ImageView vào layout
        ingredientLayout.addView(editText);
        ingredientLayout.addView(imageView);
        // Thêm layout vào container
        ingredientsContainer.addView(ingredientLayout);
    }

    private void addNewRecipe() {
        try {
            String tenMon = edtTitle.getText().toString().trim();
            String moTa = edtDescription.getText().toString().trim();
            String khauPhan = edtServing.getText().toString().trim();
            String thoiGian = edtCookingTime.getText().toString().trim();

            Log.d("AddRecipe", "Bắt đầu thêm món ăn mới");
            Log.d("AddRecipe", "Tên món: " + tenMon);
            Log.d("AddRecipe", "Mô tả: " + moTa);
            Log.d("AddRecipe", "Khẩu phần: " + khauPhan);
            Log.d("AddRecipe", "Thời gian: " + thoiGian);

            // Kiểm tra dữ liệu đầu vào
            if (tenMon.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tên món ăn!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ingredientsContainer.getChildCount() == 0) {
                Toast.makeText(this, "Vui lòng thêm ít nhất một nguyên liệu!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stepsContainer.getChildCount() == 0) {
                Toast.makeText(this, "Vui lòng thêm ít nhất một bước nấu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra xem ảnh đã upload xong chưa
            if (uploadedImageUrl == null) {
                Toast.makeText(this, "Vui lòng đợi ảnh upload xong!", Toast.LENGTH_SHORT).show();
                return;
            }

            MonAnEntity monAn = new MonAnEntity(tenMon, moTa, khauPhan, thoiGian);
            List<NguyenLieuEntity> nguyenLieus = new ArrayList<>();
            List<BuocNauEntity> buocNaus = new ArrayList<>();

            // Lấy nguyên liệu
            for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
                EditText edt = (EditText) ((LinearLayout) ingredientsContainer.getChildAt(i)).getChildAt(0);
                String text = edt.getText().toString().trim();
                if (!text.isEmpty()) {
                    NguyenLieuEntity ngl = NguyenLieuUtils.parseNguyenLieu(text);
                    if (ngl != null) {
                        nguyenLieus.add(ngl);
                        Log.d("AddRecipe", "Thêm nguyên liệu: " + ngl.toString());
                    } else {
                        Toast.makeText(this, "Nguyên liệu không hợp lệ: " + text, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }

            // Lấy bước nấu
            for (int i = 0; i < stepsContainer.getChildCount(); i++) {
                EditText edt = (EditText) ((LinearLayout) stepsContainer.getChildAt(i)).getChildAt(1);
                String text = edt.getText().toString().trim();
                if (!text.isEmpty()) {
                    BuocNauEntity buocNau = new BuocNauEntity(0, i, text);
                    buocNaus.add(buocNau);
                    Log.d("AddRecipe", "Thêm bước nấu: " + buocNau.toString());
                }
            }

            Log.d("AddRecipe", "Số lượng nguyên liệu: " + nguyenLieus.size());
            Log.d("AddRecipe", "Số lượng bước nấu: " + buocNaus.size());

            // Thêm callback để xử lý kết quả
            addRecipeVM.addNewRecipe(monAn, nguyenLieus, buocNaus, new AddRecipeVM.AddRecipeCallback() {
                @Override
                public void onSuccess() {
                    if (!isFinishing() && !isDestroyed()) {
                        runOnUiThread(() -> {
                            Log.d("AddRecipe", "Thêm món ăn thành công!");
                            Toast.makeText(AddRecipeActivity.this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }

                @Override
                public void onError(String error) {
                    if (!isFinishing() && !isDestroyed()) {
                        runOnUiThread(() -> {
                            Log.e("AddRecipe", "Lỗi khi thêm món ăn: " + error);
                            Toast.makeText(AddRecipeActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        } catch (Exception e) {
            Log.e("AddRecipe", "Lỗi trong quá trình thêm món ăn", e);
            Toast.makeText(this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    // Thêm 1 LinearLayout bước step vào stepsContainer
    public void addStepBlock() {
        // Tạo LinearLayout chứa 1 bước
        LinearLayout stepLayout = new LinearLayout(AddRecipeActivity.this);
        LinearLayout.LayoutParams stepParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        stepParams.setMargins(0, 0, 0, dpToPx(16));
        stepLayout.setLayoutParams(stepParams);
        stepLayout.setOrientation(LinearLayout.HORIZONTAL);

        // TextView - Số thứ tự bước
        TextView stepNumber = new TextView(AddRecipeActivity.this);
        LinearLayout.LayoutParams numberParams = new LinearLayout.LayoutParams(
                dpToPx(24),
                dpToPx(24)
        );
        stepNumber.setLayoutParams(numberParams);
        stepNumber.setText(String.valueOf(stepsContainer.getChildCount() + 1));
        stepNumber.setGravity(Gravity.CENTER);
        stepNumber.setTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.black));
        stepNumber.setBackground(ContextCompat.getDrawable(AddRecipeActivity.this, R.drawable.circle_background));

        // EditText - Nội dung bước
        EditText stepContent = new EditText(AddRecipeActivity.this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                0,
                dpToPx(50),
                1
        );
        editParams.setMarginStart(dpToPx(8));
        stepContent.setLayoutParams(editParams);
        stepContent.setTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.black));
        stepContent.setTypeface(ResourcesCompat.getFont(AddRecipeActivity.this, R.font.sfpro_regu));
        stepContent.setBackgroundColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.light_gray));
        stepContent.setHint("Nhập vào cách làm nhé");
        stepContent.setHintTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.gray));
        stepContent.setPadding(dpToPx(8), 0, dpToPx(8), 0);

        // ImageView - Nút xóa bước
        ImageView deleteStep = new ImageView(AddRecipeActivity.this);
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(
                dpToPx(20),
                dpToPx(50)
        );
        deleteParams.setMarginStart(dpToPx(8));
        deleteStep.setLayoutParams(deleteParams);
        deleteStep.setImageDrawable(ContextCompat.getDrawable(AddRecipeActivity.this, R.drawable.ic_delete));

        // Xử lý sự kiện xóa bước (chỉ xóa khi còn ít nhất 2 bước)
        deleteStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepsContainer.getChildCount() > 1) {
                    stepsContainer.removeView(stepLayout);
                    // Cập nhật lại số thứ tự
                    for (int i = 0; i < stepsContainer.getChildCount(); i++) {
                        View child = stepsContainer.getChildAt(i);
                        if (child instanceof LinearLayout) {
                            TextView tv = (TextView) ((LinearLayout) child).getChildAt(0);
                            tv.setText(String.valueOf(i + 1));
                        }
                    }
                } else {
                    Toast.makeText(AddRecipeActivity.this, "Cần ít nhất 1 bước!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thêm các view con vào stepLayout
        stepLayout.addView(stepNumber);
        stepLayout.addView(stepContent);
        stepLayout.addView(deleteStep);

        // Thêm stepLayout vào stepsContainer
        stepsContainer.addView(stepLayout);
    }
    private void initViews() {
        // Khởi tạo view thì thêm sẵn 2 khối nguyên liệu và 1 khối bước nấu
        addIngredientBlock();
        addIngredientBlock();
        addStepBlock();
    }
    // thêm các listener theo dõi sự thay đổi của các khối editext nhằm ràng buộc
    private void setupListeners() {
        // Tên món ăn: phải từ 2 từ trở lên
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (text.split("\\s+").length < 2) {
                    edtTitle.setError("Tên món ăn phải có ít nhất 2 từ!");
                } else {
                    edtTitle.setError(null);
                }
            }
        });

        // Khẩu phần: nếu không để trống thì phải là số
        edtServing.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                boolean hasNumberWord = false;

                if (!text.isEmpty()) {
                    String[] words = text.split("\\s+");
                    for (String word : words) {
                        // Kiểm tra nếu từ chứa toàn số
                        if (word.matches("\\d+")) {
                            hasNumberWord = true;
                            break;
                        }
                        // Kiểm tra nếu từ là biểu diễn số bằng chữ tiếng Việt
                        if (isVietnameseNumberWord(word.toLowerCase())) {
                            hasNumberWord = true;
                            break;
                        }
                    }

                    if (!hasNumberWord) {
                        edtServing.setError("Vui lòng nhập khẩu phần hợp lệ!");
                    } else {
                        edtServing.setError(null);
                    }
                } else {
                    edtServing.setError("Vui lòng không để trống!");
                }
            }

            private boolean isVietnameseNumberWord(String word) {
                String[] numberWords = {"không", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"};

                for (String numberWord : numberWords) {
                    if (word.equals(numberWord)) {
                        return true;
                    }
                }
                return false;
            }
        });

        // Thời gian: nếu không để trống thì phải chứa 1 trong các từ khóa
        edtCookingTime.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim().toLowerCase();
                String[] validWords = {"giờ", "phút", "giây", "tiếng", "h", "m", "s"};

                boolean containsKeyword = false;
                for (String word : validWords) {
                    if (text.contains(word)) {
                        containsKeyword = true;
                        break;
                    }
                }

                if (!text.isEmpty() && !containsKeyword) {
                    edtCookingTime.setError("Thời gian phải chứa đơn vị như giờ, phút, giây...");
                } else {
                    edtCookingTime.setError(null);
                }
            }
        });
    }

    private void addImageBlock(){
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
    }


    // hàm chuyển dp sang pixel
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
    public void uploadImageToCloudinary() {
        if (selectedImageUri != null) {
            CloudinaryHelper.uploadImage(this, selectedImageUri, new CloudinaryHelper.CloudinaryCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    if (!isFinishing() && !isDestroyed()) {
                        runOnUiThread(() -> {
                            uploadedImageUrl = imageUrl;
                            Toast.makeText(AddRecipeActivity.this,
                                    "Upload ảnh thành công", Toast.LENGTH_SHORT).show();
                            imgAvatar.setImageURI(selectedImageUri);
                            imgAvatar.setVisibility(View.VISIBLE);
                        });
                    }
                }

                @Override
                public void onError(String error) {
                    runOnUiThread(() -> {

                        Toast.makeText(AddRecipeActivity.this,
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
                    imgAvatar.setImageURI(selectedImageUri);
                    uploadImageToCloudinary();
                }
            } else if (requestCode == 2) { // Gallery
                selectedImageUri = data.getData();
                imgAvatar.setImageURI(selectedImageUri);
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