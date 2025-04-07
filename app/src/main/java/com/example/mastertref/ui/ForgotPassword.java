package com.example.mastertref.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mastertref.R;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password);


        /** Đặt phần quay lại cho nút quay lạị */
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView txtBack = findViewById(R.id.txtBack);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        txtBack.setOnClickListener(v -> {
            finish();
        });
    }
}