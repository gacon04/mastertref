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
import com.example.mastertref.ui.SettingFragmentChild.VeMasterTrefActivity.PhienBanApp;

public class VeMasterTref extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ve_master_tref__setting, container, false);
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

        LinearLayout llPhienBanApp = view.findViewById(R.id.ll_phienBanApp);
        llPhienBanApp.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), PhienBanApp.class);
            startActivity(intent);
        });


    }

}