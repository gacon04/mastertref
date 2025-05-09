package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mastertref.R;
import com.example.mastertref.ui.SettingFragmentChild.VeMasterTrefActivity.PhienBanApp;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class SearchFragment extends Fragment {

    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    private String username;
    private ImageView ivAvatar, closeButton;
    private TextView tvName;
    private LinearLayout llSearch;
    private CardView cvShareRecipe, cvSearch;
    private Button btnShareRecipe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        LinearLayout realSearchView = view.findViewById(R.id.layout_taikhoan);
        super.onViewCreated(view, savedInstanceState);

        ivAvatar = view.findViewById(R.id.imgProfile);
        tvName = view.findViewById(R.id.tvAppName);
        llSearch = view.findViewById(R.id.llSearch);
        cvShareRecipe = view.findViewById(R.id.cvShareRecipe);
        cvSearch = view.findViewById(R.id.cardSearch);
        btnShareRecipe = view.findViewById(R.id.btnShareNow);


        llSearch.setOnClickListener(v -> replaceFragment(new SearchDetailsFragment()));
        btnShareRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddRecipeActivity.class);
            startActivity(intent);
        });

        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> {
            cvShareRecipe.setVisibility(View.GONE);
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
                tvName.setText("Xin chào, "+user.getFullname()+"!");
                if (user.getImageLink() != null && !user.getImageLink().isEmpty()) {
                    ImageHelper.loadImage(ivAvatar, user.getImageLink());
                }

            }
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}