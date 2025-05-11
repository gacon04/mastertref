package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.DaXemGanDayAdapter;
import com.example.mastertref.data.local.Adapter.MonAnMoiNhatAdapter;
import com.example.mastertref.ui.SettingFragmentChild.VeMasterTrefActivity.PhienBanApp;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.DaXemGanDayVM;
import com.example.mastertref.viewmodel.MonAnVM;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class SearchFragment extends Fragment {

    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    private String username;
    private ImageView ivAvatar, closeButton;
    private TextView tvName, tvSeeAllRecent;
    private LinearLayout llSearch;
    private CardView cvShareRecipe, cvSearch;
    private Button btnShareRecipe;
    private RecyclerView rvNewestRec, rvRecentRec;
    private int currentUserId = -1;
    private MonAnVM monAnVM;
    private DaXemGanDayVM daXemGanDayVM;
    private MonAnMoiNhatAdapter newestRecipesAdapter;
    private DaXemGanDayAdapter recentRecipesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        daXemGanDayVM = new ViewModelProvider(this).get(DaXemGanDayVM.class);
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
        // Initialize adapters
        newestRecipesAdapter = new MonAnMoiNhatAdapter(requireContext());
        recentRecipesAdapter = new DaXemGanDayAdapter(requireContext());
        
        // Set up newest recipes RecyclerView
        rvNewestRec.setLayoutManager(new LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNewestRec.setAdapter(newestRecipesAdapter);
        
        // Set up recent recipes RecyclerView
        rvRecentRec.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRecentRec.setAdapter(recentRecipesAdapter);
        
        // Set click listeners
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
        
        recentRecipesAdapter.setOnItemClickListener(monAnId -> {
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
                // Call loadNewestRecipes and loadRecentRecipes after getting the user ID
                loadNewestRecipes();
                loadRecentRecipes();
            });
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Load lại dữ liệu mỗi khi Fragment được hiển thị lại
        loadUserData();
        // Also reload newest and recent recipes when fragment resumes
        if (currentUserId > 0) {
            loadNewestRecipes();
            loadRecentRecipes();
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
    
    private void loadRecentRecipes() {
        try {
            if (currentUserId > 0 && isAdded()) {  // Check if fragment is still attached
                daXemGanDayVM.getRecentViewsWithDetails(currentUserId).observe(getViewLifecycleOwner(), recipes -> {
                    if (recipes != null && isAdded()) {  // Double-check fragment is still attached
                        if (recipes.isEmpty()) {
                            // Hide the recent recipes section if there are no recent views
                            View recentSection = getView().findViewById(R.id.rvRecentRecipes);
                            if (recentSection != null) {
                                recentSection.setVisibility(View.GONE);
                            }
                        } else {
                            recentRecipesAdapter.setData(recipes);
                        }
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
        rvRecentRec = view.findViewById(R.id.rvRecentRecipes);
        tvSeeAllRecent = view.findViewById(R.id.tvSeeAllRecent);
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
        
        tvSeeAllRecent.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Xem tất cả món đã xem gần đây", Toast.LENGTH_SHORT).show();
            // You can implement navigation to a full list of recently viewed recipes here
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