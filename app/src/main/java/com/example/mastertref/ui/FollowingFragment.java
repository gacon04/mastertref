package com.example.mastertref.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.FollowingAdapter;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.example.mastertref.viewmodel.TheoDoiVM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FollowingFragment extends Fragment {
    private SessionManager sessionManager;
    private TaikhoanVM taikhoanVM;
    private TheoDoiVM theoDoiVM;
    private int currentUserId, showingUserId;
    private RecyclerView recyclerFollowers;
    private LinearLayout llEmptyList;
    private EditText editSearch;
    private FollowingAdapter adapter;
    private List<TaikhoanEntity> allFollowingUsers = new ArrayList<>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        theoDoiVM = new ViewModelProvider(this).get(TheoDoiVM.class);

        initCurrentUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        
        Bundle args = getArguments();
        if (args != null && args.containsKey("user_id")) {
            showingUserId = args.getInt("user_id", 0);
        } else {
            showingUserId = currentUserId;
        }
        
        setupAdapter();
        setupClickListeners();
        loadFollowingUsers();
    }

    private void setupAdapter() {
        adapter = new FollowingAdapter(requireContext(), currentUserId);
        recyclerFollowers.setAdapter(adapter);
        
        // Set up click listeners for adapter
        adapter.setOnFollowingClickListener(followingUser -> {
            // Navigate to user profile
            navigateToUserProfile(followingUser.getId());
        });
        
        adapter.setOnUnfollowButtonClickListener(followingUser -> {
            // Unfollow the user
            theoDoiVM.unfollowUser(currentUserId, followingUser.getId());
            Toast.makeText(requireContext(), "Đã bỏ theo dõi " + followingUser.getFullname(), Toast.LENGTH_SHORT).show();
            
            // Remove from the list
            adapter.removeUser(followingUser.getId());
            allFollowingUsers.removeIf(user -> user.getId() == followingUser.getId());
            
            // Update empty state
            updateEmptyState();
        });
    }

    private void navigateToUserProfile(int userId) {
        // Navigate to user profile
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        ShowOthersPro5Fragment fragment = new ShowOthersPro5Fragment();
        fragment.setArguments(args);
        
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupClickListeners() {
        // Set up search functionality
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFollowingUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void filterFollowingUsers(String query) {
        if (query.isEmpty()) {
            adapter.setFollowingUsers(allFollowingUsers);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            List<TaikhoanEntity> filteredList = allFollowingUsers.stream()
                    .filter(user -> 
                            user.getFullname().toLowerCase().contains(lowerCaseQuery) ||
                            user.getUsername().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
            adapter.setFollowingUsers(filteredList);
        }
        
        updateEmptyState();
    }
    
    private void loadFollowingUsers() {
        // Load users that the showing user is following
        theoDoiVM.getFollowingUserIds(showingUserId).observe(getViewLifecycleOwner(), followingIds -> {
            if (followingIds != null && !followingIds.isEmpty()) {
                // Load user details for each following ID
                taikhoanVM.getUsersByIds(followingIds).observe(getViewLifecycleOwner(), followingUsers -> {
                    allFollowingUsers = followingUsers;
                    adapter.setFollowingUsers(followingUsers);
                    updateEmptyState();
                });
            } else {
                allFollowingUsers.clear();
                adapter.setFollowingUsers(allFollowingUsers);
                updateEmptyState();
            }
        });
    }
    
    private void updateEmptyState() {
        if (adapter.getItemCount() == 0) {
            recyclerFollowers.setVisibility(View.GONE);
            llEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerFollowers.setVisibility(View.VISIBLE);
            llEmptyList.setVisibility(View.GONE);
        }
    }
    
    private void initViews(View view) {
        // Initialize UI components
        ImageButton btnBack = view.findViewById(R.id.btnBack);
        TextView txtTitle = view.findViewById(R.id.txtTitle);
        editSearch = view.findViewById(R.id.editSearch);
        recyclerFollowers = view.findViewById(R.id.recyclerFollowers);
        llEmptyList = view.findViewById(R.id.llEmptyList);
        
        // Set title
        txtTitle.setText("Đang theo dõi");
        
        // Configure RecyclerView
        recyclerFollowers.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        // Set back button click listener
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());
    }
    
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
                // If we already have the adapter set up, update it with the current user ID
                if (adapter != null) {
                    adapter = new FollowingAdapter(requireContext(), currentUserId);
                    recyclerFollowers.setAdapter(adapter);
                    setupAdapter();
                    loadFollowingUsers();
                }
            });
        }
    }
}