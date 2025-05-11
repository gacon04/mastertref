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
import com.example.mastertref.data.local.Adapter.SearchPeopleResultAdapter;
import com.example.mastertref.data.local.Adapter.SearchRecipeResultAdapter;
import com.example.mastertref.data.local.LichsuTimkiemEntity;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.LichSuTimKiemVM;
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

    private ImageButton btnBack, btnClearSearch, btnFilter;
    private EditText etSearch;
    private TabLayout tabLayout;
    private RecyclerView rvSearchResults;
    private LinearLayout emptyStateContainer;
    private SessionManager sessionManager;

    // ViewModel
    private MonAnVM monAnVM;
    private TaikhoanVM taikhoanVM;
    // Adapter
    private SearchRecipeResultAdapter searchAdapter;

    // Search parameters
    private String currentQuery = "";
    private int currentTabPosition = 0;
    private static final int FILTER_RELEVANCE = 0;
    private static final int FILTER_NEWEST = 1;
    private static final int FILTER_OLDEST = 2;
    private static final int FILTER_A_TO_Z = 3;
    private static final int FILTER_Z_TO_A = 4;

    // Add this field to track current filter option
    private int currentFilterOption = FILTER_RELEVANCE;

    // Current user ID
    private int currentUserId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        
        // Initialize current user ID
        initCurrentUserId();
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
            
            // lưu lịch sử tìm kiếm
            if (!currentQuery.isEmpty()) {
                saveSearchQuery(currentQuery);
            }
            
            performSearch(currentQuery);
        }
    }
    
    // từ khóa tìm kiếm mới nhất của người dùng mà trùng với từ hiện tại thì không insert nữa, còn từ đã insert lâu rồi thì có thể xóa và insert từ này lại vào
    private void saveSearchQuery(String query) {
        if (query.isEmpty() || currentUserId <= 0) {
            return;
        }
        
        // Initialize LichSuTimKiemVM if needed
        LichSuTimKiemVM lichSuTimKiemVM = new ViewModelProvider(this).get(LichSuTimKiemVM.class);
        
        // Use the new method that handles everything in one transaction
        lichSuTimKiemVM.saveUniqueSearchQuery(currentUserId, query);
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
            // Navigate to SearchDetailsFragment
            SearchDetailsFragment searchDetailsFragment = new SearchDetailsFragment();
            
            // If we want to preserve the current search query
            Bundle args = new Bundle();
            args.putString("search_query", currentQuery);
            searchDetailsFragment.setArguments(args);
            
            // Replace current fragment with SearchDetailsFragment
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, searchDetailsFragment)
                .addToBackStack(null)
                .commit();
                
            // Disable the EditText to prevent keyboard from showing up
            // before navigation completes
            etSearch.setEnabled(false);
            
            // Re-enable after a short delay
            etSearch.postDelayed(() -> etSearch.setEnabled(true), 200);
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
    private void showFilterOptions() {
        PopupMenu popup = new PopupMenu(requireContext(), btnFilter);
        Menu menu = popup.getMenu();

        // Add filter options based on current tab
        if (currentTabPosition == 0) {
            // Recipe filters
            menu.add(Menu.NONE, FILTER_RELEVANCE, Menu.NONE, "Liên quan nhất");
            menu.add(Menu.NONE, FILTER_NEWEST, Menu.NONE, "Mới nhất");
            menu.add(Menu.NONE, FILTER_OLDEST, Menu.NONE, "Cũ nhất");
            menu.add(Menu.NONE, FILTER_A_TO_Z, Menu.NONE, "A đến Z");
            menu.add(Menu.NONE, FILTER_Z_TO_A, Menu.NONE, "Z đến A");
        } else {
            // User filters
            menu.add(Menu.NONE, FILTER_RELEVANCE, Menu.NONE, "Liên quan nhất");
            menu.add(Menu.NONE, FILTER_A_TO_Z, Menu.NONE, "A đến Z");
            menu.add(Menu.NONE, FILTER_Z_TO_A, Menu.NONE, "Z đến A");
        }

        // Set click listener for menu items
        popup.setOnMenuItemClickListener(item -> {
            currentFilterOption = item.getItemId();

            // Update search results with new filter
            if (!currentQuery.isEmpty()) {
                performSearch(currentQuery);
            }

            return true;
        });

        // Show the popup menu
        popup.show();
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

    // Add the performSearch method to handle searching based on current tab
    private void performSearch(String query) {
        if (query.isEmpty()) {
            showEmptyState(true);
            return;
        }
        
        // Perform search based on current tab position
        if (currentTabPosition == 0) {
            // Tab 0: "CÔNG THỨC" - Search for recipes
            searchRecipes(query);
        } else {
            // Tab 1: "NGƯỜI DÙNG" - Search for users
            searchUsers(query);
        }
    }

    // Add this field with other adapter declarations
    private SearchPeopleResultAdapter userAdapter;

    // Update the setupRecyclerView method to initialize both adapters
    private void setupRecyclerView() {
        // Initialize recipe adapter
        searchAdapter = new SearchRecipeResultAdapter(requireContext());

        // Initialize user adapter
        userAdapter = new SearchPeopleResultAdapter(requireContext());

        // Set up RecyclerView with recipe adapter initially
        rvSearchResults.setAdapter(searchAdapter);
        rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // Set item click listener for recipes
        searchAdapter.setOnItemClickListener(MonAnWithChiTiet -> {
            // Navigate to recipe detail
            navigateToRecipeDetail(MonAnWithChiTiet.getMonAn().getId());
        });

        // Set click listeners for user adapter
        userAdapter.setOnUserClickListener(user -> {
            // Navigate to user profile
            navigateToUserProfile(user.getId());
        });

        userAdapter.setOnFollowClickListener((user, position) -> {
            // Handle follow/unfollow action
            Toast.makeText(requireContext(), "Đã theo dõi " + user.getUsername(), Toast.LENGTH_SHORT).show();
            // Here you would implement the actual follow functionality
        });
    }

    // Update the searchUsers method to actually search for users
    private void searchUsers(String query) {
        if (query.isEmpty()) {
            showEmptyState(true);
            return;
        }

        // Change layout manager for user list (1 column instead of 2)
        rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        // Set user adapter to RecyclerView
        rvSearchResults.setAdapter(userAdapter);

        // Use the appropriate search method based on filter
        switch (currentFilterOption) {
            case FILTER_A_TO_Z:
                taikhoanVM.searchUsersByUsernameOrNameAZ(query, currentUserId).observe(getViewLifecycleOwner(), this::handleUserSearchResults);
                break;
            case FILTER_Z_TO_A:
                taikhoanVM.searchUsersByUsernameOrNameZA(query, currentUserId).observe(getViewLifecycleOwner(), this::handleUserSearchResults);
                break;
            case FILTER_RELEVANCE:
            default:
                // Default sorting by relevance
                taikhoanVM.searchUsersByUsernameOrName(query, currentUserId).observe(getViewLifecycleOwner(), this::handleUserSearchResults);
                break;
        }
    }

    // Add this method to handle user search results
    private void handleUserSearchResults(List<TaikhoanEntity> users) {
        if (users != null && !users.isEmpty()) {
            showEmptyState(false);
            userAdapter.setData(users);
        } else {
            showEmptyState(true);
        }
    }

    // Update the searchRecipes method to set the correct adapter and layout
    private void searchRecipes(String query) {
        // Set recipe adapter to RecyclerView
        rvSearchResults.setAdapter(searchAdapter);
        rvSearchResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));

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

    // Add this method to navigate to user profile
    private void navigateToUserProfile(int userId) {
        Bundle args = new Bundle();
        args.putInt("user_id", userId);

        // Tạo fragment mới và truyền bundle
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(args);

        // Chuyển đến SearchResultFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, ShowOthersPro5Fragment.class, args)
                .addToBackStack(null)
                .commit();

    }

    // Fix the getCurrentUserId method to properly get the user ID
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
                // If we already have a search query, refresh the search with the correct user ID
                if (!currentQuery.isEmpty()) {
                    performSearch(currentQuery);
                }
            });
        }
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


}


