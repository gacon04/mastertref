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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.FollowerAdapter;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.example.mastertref.viewmodel.TheoDoiVM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FollowersFragment extends Fragment {
    private SessionManager sessionManager;
    private TaikhoanVM taikhoanVM;
    private TheoDoiVM theoDoiVM;
    private int currentUserId, showingUserId;
    private RecyclerView recyclerFollowers;
    private LinearLayout llEmptyList;
    private EditText editSearch;
    private FollowerAdapter adapter;
    private List<TaikhoanEntity> allFollowers = new ArrayList<>();
    
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
        loadFollowers();
    }

    private void setupAdapter() {
        adapter = new FollowerAdapter(requireContext(), currentUserId);
        recyclerFollowers.setAdapter(adapter);
        
        // Set up click listeners for adapter
        adapter.setOnFollowerClickListener(follower -> {
            // Navigate to user profile
            navigateToUserProfile(follower.getId());
        });
        
        adapter.setOnFollowButtonClickListener((follower, isFollowing) -> {
            if (isFollowing) {
                theoDoiVM.followUser(currentUserId, follower.getId());
                Toast.makeText(requireContext(), "Đã theo dõi " + follower.getFullname(), Toast.LENGTH_SHORT).show();
            } else {
                theoDoiVM.unfollowUser(currentUserId, follower.getId());
                Toast.makeText(requireContext(), "Đã hủy theo dõi " + follower.getFullname(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToUserProfile(int userId) {
        // Navigate to user profile (implement based on your app's navigation)
        // For example:
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
                filterFollowers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void filterFollowers(String query) {
        if (query.isEmpty()) {
            adapter.setFollowers(allFollowers);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            List<TaikhoanEntity> filteredList = allFollowers.stream()
                    .filter(follower -> 
                            follower.getFullname().toLowerCase().contains(lowerCaseQuery) ||
                            follower.getUsername().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
            adapter.setFollowers(filteredList);
        }
        
        updateEmptyState();
    }
    
    private void loadFollowers() {
        // Load followers for the showing user
        theoDoiVM.getFollowerUserIds(showingUserId).observe(getViewLifecycleOwner(), followerIds -> {
            if (followerIds != null && !followerIds.isEmpty()) {
                // Load user details for each follower ID
                taikhoanVM.getUsersByIds(followerIds).observe(getViewLifecycleOwner(), followers -> {
                    allFollowers = followers;
                    adapter.setFollowers(followers);
                    updateEmptyState();
                    
                    // Load following status for current user
                    loadFollowingStatus();
                });
            } else {
                allFollowers.clear();
                adapter.setFollowers(allFollowers);
                updateEmptyState();
            }
        });
    }
    
    private void loadFollowingStatus() {
        // Load who the current user is following to update button states
        theoDoiVM.getFollowingUserIds(currentUserId).observe(getViewLifecycleOwner(), followingIds -> {
            if (followingIds != null) {
                adapter.setFollowingIds(followingIds);
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
        txtTitle.setText("Người theo dõi");

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
                    adapter = new FollowerAdapter(requireContext(), currentUserId);
                    recyclerFollowers.setAdapter(adapter);
                    setupAdapter();
                    loadFollowers();
                }
            });
        }
    }
}