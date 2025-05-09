package com.example.mastertref.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mastertref.R;

public class CommentAcitivity extends AppCompatActivity {
    private ImageButton btnBack, btnCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);

    }
    private void initViews() {
        btnBack = findViewById(R.id.backButton);
        btnCamera = findViewById(R.id.cameraButton);
    }
    private void setupListeners() {
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}