package com.example.mastertref.SettingFragmentChild;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mastertref.R;

public class DieuKhoanChinhSach extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dieu_khoan_chinh_sach__setting, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cài đặt cho nút back
        ImageView backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        // Gán các LinearLayout bằng các link cụ thể
        LinearLayout llDieuKhoanBM = view.findViewById(R.id.ll_dieuKhoanBaoMat);
        LinearLayout llDieuKhoanDV = view.findViewById(R.id.ll_dieuKhoanDichVu);
        LinearLayout llHuongDanCongDong= view.findViewById(R.id.ll_huongDanChoCongDong);

        llDieuKhoanBM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cookpad.com/vn/bao-mat";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        llDieuKhoanDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cookpad.com/vn/dieu-khoan";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        llHuongDanCongDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://cookpad.com/vn/cong-dong";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

}