package com.example.mastertref.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mastertref.R;


public class SearchDetailsFragment extends Fragment {


    private TextView btnThoat;
    private EditText edtSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_details, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnThoat = view.findViewById(R.id.btnThoat);
        edtSearch = view.findViewById(R.id.edtSearch);
        
        setupListeners();
    }

    private void setupListeners() {
        // Thêm TextWatcher để theo dõi thay đổi trong edtSearch
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu có text, đổi btnThoat thành "x"
                if (s.length() > 0) {
                    btnThoat.setText("x");
                } else {
                    btnThoat.setText("Thoát");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện click cho btnThoat
        btnThoat.setOnClickListener(v -> {
            // Nếu đang hiển thị "x" (có text trong edtSearch)
            if (btnThoat.getText().toString().equals("x")) {
                edtSearch.setText("");
            } else {
                // Nếu không có text, quay lại Fragment trước đó
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}