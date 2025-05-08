package com.example.mastertref.ui.SettingFragmentChild.VeMasterTrefActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mastertref.R;
import com.example.mastertref.utils.SessionManager;

public class PhienBanApp extends AppCompatActivity {
    private SessionManager sessionManager;
    private TextView tvUserID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phien_ban_app);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            finish();
        });
        sessionManager = new SessionManager(this);
        tvUserID = findViewById(R.id.tv_id_user);
        tvUserID.setText("Mã ID của bạn: " + sessionManager.getUsername());
    }
}