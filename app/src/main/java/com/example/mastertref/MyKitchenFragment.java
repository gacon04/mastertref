package com.example.mastertref;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class MyKitchenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        return inflater.inflate(R.layout.fragment_my_kitchen, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm nút trong layout fragment
        Button suaThongTinButton = view.findViewById(R.id.btn_edit_info_button);

        // Bắt sự kiện click và mở Activity mới
        suaThongTinButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeMyInfo.class);
            startActivity(intent);
        });
    }
}
