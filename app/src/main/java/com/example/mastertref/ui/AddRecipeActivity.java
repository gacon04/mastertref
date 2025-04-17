package com.example.mastertref.ui;

import android.os.Bundle;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.mastertref.R;
import com.example.mastertref.databinding.ActivityAddRecipeBinding;

public class AddRecipeActivity extends AppCompatActivity {

    private ActivityAddRecipeBinding binding;
    Button  btnAddMoreIngredient, btnAddMoreStep, btnAddRecipe;
    ImageButton btnClose;
    Button btnAddIngredient, btnAddStep;
    LinearLayout ingredientsContainer, stepsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnClose = findViewById(R.id.btn_Close);
        btnAddStep = findViewById(R.id.btnAddStep);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        stepsContainer = findViewById(R.id.stepsContainer); // Tách ra dùng chung
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
        editText.setHint("Nhập tên nguyên liệu nhé");
        editText.setHintTextColor(ContextCompat.getColor(AddRecipeActivity.this, R.color.gray));
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
        // Khởi tạo view thì thêm sẵn 2 khối nguyên liệu
        addIngredientBlock();
        addIngredientBlock();
        addStepBlock();

    }

    private void setupListeners() {
        // Implementation of setupListeners method
    }
    // hàm chuyển dp sang pixel
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

}