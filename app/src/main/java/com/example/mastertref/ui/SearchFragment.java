package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.MonAnMoiNhatAdapter;
import com.example.mastertref.ui.SettingFragmentChild.VeMasterTrefActivity.PhienBanApp;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;
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
    private RecyclerView rvNewestRec;
    private int currentUserId = -1;
    private MonAnVM monAnVM;
    private MonAnMoiNhatAdapter newestRecipesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);  // Move this to the top

        initView(view);
        setupClickListeners();
        setupRecyclerView();  // Set up RecyclerView before loading data
        
        // Get current user ID and load data
        initCurrentUserId();
        loadUserData();
    }
    
    private void setupRecyclerView() {
        // Initialize adapter
        newestRecipesAdapter = new MonAnMoiNhatAdapter(requireContext());
        
        // Set up RecyclerView
        rvNewestRec.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(
                requireContext(), androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false));
        rvNewestRec.setAdapter(newestRecipesAdapter);
        
        // Set click listener
        newestRecipesAdapter.setOnItemClickListener(monAnId -> {
            try {
                // Navigate to recipe detail
                Intent intent = new Intent(requireContext(), ChiTietMonAnActivity.class);
                intent.putExtra("mon_an_id", monAnId);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void initCurrentUserId() {
        username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
                // Call loadNewestRecipes after getting the user ID
                loadNewestRecipes();
            });
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Load lại dữ liệu mỗi khi Fragment được hiển thị lại
        loadUserData();
        // Also reload newest recipes when fragment resumes
        if (currentUserId > 0) {
            loadNewestRecipes();
        }
    }
    private void loadNewestRecipes() {
        try {
            if (currentUserId > 0 && isAdded()) {  // Check if fragment is still attached
                monAnVM.getNewestRecipesFiltered(currentUserId, 8).observe(getViewLifecycleOwner(), recipes -> {
                    if (recipes != null && isAdded()) {  // Double-check fragment is still attached
                        newestRecipesAdapter.setData(recipes);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initView(View view) {
        ivAvatar = view.findViewById(R.id.imgProfile);
        tvName = view.findViewById(R.id.tvAppName);
        llSearch = view.findViewById(R.id.llSearch);
        cvShareRecipe = view.findViewById(R.id.cvShareRecipe);
        cvSearch = view.findViewById(R.id.cardSearch);
        btnShareRecipe = view.findViewById(R.id.btnShareNow);
        closeButton = view.findViewById(R.id.closeButton);
        rvNewestRec = view.findViewById(R.id.rvNewestRecipes);
    }
    private void setupClickListeners() {

        llSearch.setOnClickListener(v -> replaceFragment(new SearchDetailsFragment()));
        btnShareRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddRecipeActivity.class);
            startActivity(intent);
        });
        closeButton.setOnClickListener(v -> {
            cvShareRecipe.setVisibility(View.GONE);
        });
    }


    private void loadUserData() {
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