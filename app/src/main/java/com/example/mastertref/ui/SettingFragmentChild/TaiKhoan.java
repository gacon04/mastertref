package com.example.mastertref.ui.SettingFragmentChild;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mastertref.R;
import com.example.mastertref.ui.SettingFragmentChild.TaiKhoanActivity.CacBepDaChan;
import com.example.mastertref.ui.SettingFragmentChild.TaiKhoanActivity.DoiMatKhau;

public class TaiKhoan extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tai_khoan__setting, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm ImageView trong layout fragment
        ImageView backButton = view.findViewById(R.id.back_button);

        // Bắt sự kiện click và quay lại Fragment trước đó
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        LinearLayout llCacBepDaChan = view.findViewById(R.id.llCacBepDaChan);
        llCacBepDaChan.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CacBepDaChan.class);
            startActivity(intent);
        });


        LinearLayout llDoiMatKhau = view.findViewById(R.id.llDoiMatKhau);
        llDoiMatKhau.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), DoiMatKhau.class);
            startActivity(intent);
        });
    }


}