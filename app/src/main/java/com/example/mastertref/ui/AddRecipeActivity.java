package com.example.mastertref.ui;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mastertref.utils.NguyenLieuUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.data.local.BuocNauEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.NguyenLieuEntity;
import com.example.mastertref.databinding.ActivityAddRecipeBinding;
import com.example.mastertref.viewmodel.AddRecipeVM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {
    private ActivityAddRecipeBinding binding;
    Button  btnAddMoreIngredient, btnAddMoreStep, btnAddRecipe;
    ImageButton btnClose;
    Button btnAddIngredient, btnAddStep, btnPost, btnDraft;
    LinearLayout ingredientsContainer, stepsContainer;
    EditText edtTitle, edtDescription, edtServing, edtCookingTime;
    ImageView imgAvatar,btnAddImage;
    private AddRecipeVM viewModel;
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

        viewModel = new ViewModelProvider(this).get(AddRecipeVM.class);

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
        String tenMon = edtTitle.getText().toString().trim();
        String moTa = edtDescription.getText().toString().trim();
        String khauPhan = edtServing.getText().toString().trim();
        String thoiGian = edtCookingTime.getText().toString().trim();


        if (tenMon.isEmpty()) {
            Toast.makeText(this, "Nhập tên món ăn!", Toast.LENGTH_SHORT).show();
            return;
        }

        MonAnEntity monAn = new MonAnEntity(tenMon, moTa, khauPhan, thoiGian); // nếu bạn có constructor
        List<NguyenLieuEntity> nguyenLieus = new ArrayList<>();
        List<BuocNauEntity> buocNaus = new ArrayList<>();

        // Lấy nguyên liệu
        for (int i = 0; i < ingredientsContainer.getChildCount(); i++) {
            EditText edt = (EditText) ((LinearLayout) ingredientsContainer.getChildAt(i)).getChildAt(0);
            String text = edt.getText().toString().trim();
            if (!text.isEmpty()) {
                // PHÂN TÁCH NGUYÊN LIỆU THÀNH ĐỊNH LƯỢNG RIÊNG VÀ TÊN NGUYÊN LIỆU RIÊNG
                NguyenLieuEntity ngl = NguyenLieuUtils.parseNguyenLieu(text);
                if (ngl != null) {
                    nguyenLieus.add(ngl);
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
                buocNaus.add(new BuocNauEntity(i + 1, text));
            }
        }

        viewModel.addNewRecipe(monAn, nguyenLieus, buocNaus);
        Toast.makeText(this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }


    // Thêm 1 LinearLayout step vào stepsContainer
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
                if (!text.isEmpty() && !text.matches("\\d+")) {
                    edtServing.setError("Vui lòng nhập số lượng người ăn!");
                } else {
                    edtServing.setError(null);
                }
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




    // hàm chuyển dp sang pixel
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}