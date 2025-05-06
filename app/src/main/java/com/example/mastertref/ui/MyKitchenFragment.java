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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.MonAnAdapter;
import com.example.mastertref.domain.models.TaiKhoanDTO;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class MyKitchenFragment extends Fragment {
    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    TextView tvHoten, tvUsername;
    ImageView ivAvatar;
    String username;
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

        MonAnAdapter adapter = new MonAnAdapter(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.rvMonAn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        MonAnVM viewModel = new ViewModelProvider(this).get(MonAnVM.class);
        viewModel.getMonAnWithChiTietByUsername(sessionManager.getUsername())
                .observe(getViewLifecycleOwner(), monAnWithChiTietList -> {
                    adapter.setData(monAnWithChiTietList);
                });

        adapter.setOnItemClickListener(monAnChiTiet -> {
            if (monAnChiTiet != null && monAnChiTiet.getMonAn() != null) {
                Intent intent = new Intent(requireContext(), ChiTietMonAnActivity.class);
                intent.putExtra("mon_an_id", monAnChiTiet.getMonAn().getId());
                startActivity(intent);
            } else {
                // Handle the case where monAnChiTiet or its MonAn is null
                Toast.makeText(requireContext(), "Không thể mở chi tiết món ăn", Toast.LENGTH_SHORT).show();
            }
        });



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
        username = sessionManager.getUsername();
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
