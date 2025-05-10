package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.SearchResultAdapter;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

// Add these imports at the top of the file
import android.view.Menu;
import android.widget.PopupMenu;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class SearchResultFragment extends Fragment {
    // UI elements
    private TaikhoanVM taikhoanVM;
    private ImageButton btnBack, btnClearSearch, btnFilter;
    private EditText etSearch;
    private TabLayout tabLayout;
    private RecyclerView rvSearchResults;
    private LinearLayout emptyStateContainer;
    private SessionManager sessionManager;
    
    // ViewModel
    private MonAnVM monAnVM;
    
    // Adapter
    private SearchResultAdapter searchAdapter;
    
    // Search parameters
    private String currentQuery = "";
    private int currentTabPosition = 0;
    
    // Current user ID
    private int currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        // Get current user ID from shared preferences or user session
        currentUserId = getCurrentUserId();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize UI elements
        initViews(view);
        
        // Set up click listeners
        setupClickListeners();
        
        // Set up tab layout
        setupTabLayout();
        
        // Set up RecyclerView
        setupRecyclerView();


        // Get search query from arguments if available
        Bundle args = getArguments();
        if (args != null && args.containsKey("search_query")) {
            currentQuery = args.getString("search_query", "");
            etSearch.setText(currentQuery);
            performSearch(currentQuery);
        }
    }
    
    private void initViews(View view) {
        btnBack = view.findViewById(R.id.btn_back);
        btnClearSearch = view.findViewById(R.id.btn_clear_search);
        btnFilter = view.findViewById(R.id.btn_filter);
        etSearch = view.findViewById(R.id.et_search);
        tabLayout = view.findViewById(R.id.tab_layout);
        rvSearchResults = view.findViewById(R.id.rv_search_results);
        emptyStateContainer = view.findViewById(R.id.empty_state_container);
    }
    
    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            // Navigate back
            requireActivity().onBackPressed();
        });
        
        btnClearSearch.setOnClickListener(v -> {
            // Clear search text
            etSearch.setText("");
            currentQuery = "";
            showEmptyState(true);
        });
        
        btnFilter.setOnClickListener(v -> {
            // Show filter options
            showFilterOptions();
        });
        
        // Add click listener to search field to navigate back to SearchDetailsFragment
        etSearch.setOnClickListener(v -> {
            // Navigate back to previous fragment (SearchDetailsFragment)
            requireActivity().onBackPressed();
        });
        
        // Add text change listener to search field
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Update search when text changes
                if (s.toString().isEmpty()) {
                    btnClearSearch.setVisibility(View.GONE);
                    showEmptyState(true);
                } else {
                    btnClearSearch.setVisibility(View.VISIBLE);
                    currentQuery = s.toString();
                    performSearch(currentQuery);
                }
            }
        });
    }
    
    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabPosition = tab.getPosition();
                if (!currentQuery.isEmpty()) {
                    performSearch(currentQuery);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }
    
    private void setupRecyclerView() {
        // Initialize adapter
        searchAdapter = new SearchResultAdapter(requireContext());
        
        // Set up RecyclerView
        rvSearchResults.setAdapter(searchAdapter);
        rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        
        // Set item click listener
        searchAdapter.setOnItemClickListener(MonAnWithChiTiet -> {
            // Navigate to recipe detail
            navigateToRecipeDetail(MonAnWithChiTiet.getMonAn().getId());
        });
    }
    
    private void performSearch(String query) {
        if (query.isEmpty()) {
            showEmptyState(true);
            return;
        }
        
        // Determine which tab is selected and perform appropriate search
        if (currentTabPosition == 0) {
            // Search recipes
            searchRecipes(query);
        } else {
            // Search users
            searchUsers(query);
        }
    }
    
    // Add these fields at the top of the class with other fields
    private static final int FILTER_RELEVANCE = 1;
    private static final int FILTER_NEWEST = 2;
    private static final int FILTER_OLDEST = 3;
    private static final int FILTER_A_TO_Z = 4;
    private static final int FILTER_Z_TO_A = 5;
    private void showFilterOptions() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), btnFilter);
        popupMenu.getMenu().add(Menu.NONE, FILTER_RELEVANCE, Menu.NONE, "Relevance");
        popupMenu.getMenu().add(Menu.NONE, FILTER_NEWEST, Menu.NONE, "Mới nhất");
        popupMenu.getMenu().add(Menu.NONE, FILTER_OLDEST, Menu.NONE, "Cũ nhất");
        popupMenu.getMenu().add(Menu.NONE, FILTER_A_TO_Z, Menu.NONE, "A -> Z");
        popupMenu.getMenu().add(Menu.NONE, FILTER_Z_TO_A, Menu.NONE, "Z -> A");

        // Mark the current selection
        popupMenu.getMenu().getItem(currentFilterOption - 1).setChecked(true);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id != currentFilterOption) {
                currentFilterOption = id;

                // If there's an active search, reapply it with the new filter
                if (!currentQuery.isEmpty()) {
                    performSearch(currentQuery);
                }

                // Show toast message based on selected filter
                String filterMessage;
                switch (currentFilterOption) {
                    case FILTER_RELEVANCE:
                        filterMessage = "Sắp xếp theo độ liên quan";
                        break;
                    case FILTER_NEWEST:
                        filterMessage = "Sắp xếp theo mới nhất";
                        break;
                    case FILTER_OLDEST:
                        filterMessage = "Sắp xếp theo cũ nhất";
                        break;
                    case FILTER_A_TO_Z:
                        filterMessage = "Sắp xếp theo tên A -> Z";
                        break;
                    case FILTER_Z_TO_A:
                        filterMessage = "Sắp xếp theo tên Z -> A";
                        break;
                    default:
                        filterMessage = "Đã áp dụng bộ lọc";
                }
                Toast.makeText(requireContext(), filterMessage, Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        popupMenu.show();
    }
    
    // Current filter option, default is Relevance
    private int currentFilterOption = FILTER_RELEVANCE;

    private void searchRecipes(String query) {
        // Use the method that filters out blocked users
        monAnVM.searchRecipesByNameOrIngredient(query, currentUserId).observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null && !recipes.isEmpty()) {
                // Apply sorting based on current filter option
                List<MonAnWithChiTiet> sortedRecipes = new ArrayList<>(recipes);

                switch (currentFilterOption) {
                    case FILTER_NEWEST:
                        // Sort by newest using createAt field (timestamp)
                        Collections.sort(sortedRecipes, (a, b) -> {
                            long timeA = a.getMonAn().getCreateAt();
                            long timeB = b.getMonAn().getCreateAt();
                            // Sort descending (newest first)
                            return Long.compare(timeB, timeA);
                        });
                        break;
                    case FILTER_OLDEST:
                        // Sort by oldest using createAt field
                        Collections.sort(sortedRecipes, (a, b) -> {
                            long timeA = a.getMonAn().getCreateAt();
                            long timeB = b.getMonAn().getCreateAt();
                            // Sort ascending (oldest first)
                            return Long.compare(timeA, timeB);
                        });
                        break;
                    case FILTER_A_TO_Z:
                        // Sort alphabetically A-Z
                        Collections.sort(sortedRecipes, (a, b) -> {
                            String nameA = a.getMonAn().getTenMonAn();
                            String nameB = b.getMonAn().getTenMonAn();
                            // Handle null names
                            if (nameA == null && nameB == null) return 0;
                            if (nameA == null) return -1;
                            if (nameB == null) return 1;
                            // Sort ascending (A-Z)
                            return nameA.compareToIgnoreCase(nameB);
                        });
                        break;
                    case FILTER_Z_TO_A:
                        // Sort alphabetically Z-A
                        Collections.sort(sortedRecipes, (a, b) -> {
                            String nameA = a.getMonAn().getTenMonAn();
                            String nameB = b.getMonAn().getTenMonAn();
                            // Handle null names
                            if (nameA == null && nameB == null) return 0;
                            if (nameA == null) return 1;
                            if (nameB == null) return -1;
                            // Sort descending (Z-A)
                            return nameB.compareToIgnoreCase(nameA);
                        });
                        break;
                    case FILTER_RELEVANCE:
                    default:
                        // Use default order (relevance) from the repository
                        break;
                }

                showEmptyState(false);
                searchAdapter.setData(sortedRecipes);
            } else {
                showEmptyState(true);
            }
        });
    }
    
    private void searchUsers(String query) {
        // This would be implemented in your ViewModel
        // For now, just show empty state
        showEmptyState(true);
        
        // When you implement user search:
        /*
        userViewModel.searchUsers(query).observe(getViewLifecycleOwner(), users -> {
            if (users != null && !users.isEmpty()) {
                showEmptyState(false);
                userAdapter.setData(users);
            } else {
                showEmptyState(true);
            }
        });
        */
    }
    
    private void showEmptyState(boolean show) {
        if (show) {
            rvSearchResults.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            rvSearchResults.setVisibility(View.VISIBLE);
            emptyStateContainer.setVisibility(View.GONE);
        }
    }
    
    private void navigateToRecipeDetail(int recipeId) {
        Intent intent = new Intent(requireContext(), ChiTietMonAnActivity.class);
        intent.putExtra("mon_an_id", recipeId);
        startActivity(intent);
    }
    
    // Helper method to get current user ID
    private int getCurrentUserId() {
        String username = sessionManager.getUsername();
        AtomicInteger result = new AtomicInteger(-1);
        try {
            // Note: This is still asynchronous and won't work as expected
            taikhoanVM.getUserByUsername(username).observe(this, user -> {
                if (user != null) {
                    result.set(user.getId());
                }
            });
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
        return result.get(); // This will likely return -1 as the observer hasn't run yet
    }
}