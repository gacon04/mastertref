package com.example.mastertref.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mastertref.R;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class MyKitchenFragment extends Fragment {
    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    TextView tvHoten, tvUsername;
    ImageView ivAvatar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo SessionManager và TaikhoanVM
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        
        return inflater.inflate(R.layout.fragment_my_kitchen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo views
        tvHoten = view.findViewById(R.id.tv_hoten);
        tvUsername = view.findViewById(R.id.tv_username);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        Button suaThongTinButton = view.findViewById(R.id.btn_edit_info_button);

        // Bắt sự kiện click và mở Activity mới
        suaThongTinButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeMyInfo.class);
            startActivity(intent);
        });

        // Load dữ liệu
        loadUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Load lại dữ liệu mỗi khi Fragment được hiển thị lại
        loadUserData();
    }

    private void loadUserData() {
        String username = sessionManager.getUsername();
        taikhoanVM.getUserByUsername(username).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                tvHoten.setText(user.getFullname());
                tvUsername.setText("@" + user.getUsername());
                if (user.getImageLink() != null && !user.getImageLink().isEmpty()) {
                    ImageHelper.loadImage(ivAvatar, user.getImageLink());
                }

            }
        });
    }
}
