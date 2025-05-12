package com.example.mastertref.ui.SettingFragmentChild.TaiKhoanActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.BlockAdapter;
import com.example.mastertref.data.local.TaikhoanEntity;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.ChanTaiKhoanVM;
import com.example.mastertref.viewmodel.TaikhoanVM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CacBepDaChan extends AppCompatActivity {
    private SessionManager sessionManager;
    private TaikhoanVM taikhoanVM;
    private ChanTaiKhoanVM chanTaiKhoanVM;
    private int currentUserId;
    private RecyclerView recyclerFollowers;
    private LinearLayout llEmptyList;
    private EditText editSearch;
    private BlockAdapter adapter;
    private List<TaikhoanEntity> allBlockedUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cac_bep_da_chan);

        // Initialize ViewModels
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        chanTaiKhoanVM = new ViewModelProvider(this).get(ChanTaiKhoanVM.class);
        sessionManager = new SessionManager(this);

        // Initialize views
        initViews();
        
        // Get current user ID
        initCurrentUserId();
    }

    private void initViews() {
        // Initialize UI components
        ImageButton btnBack = findViewById(R.id.btnBack);
        editSearch = findViewById(R.id.editSearch);
        recyclerFollowers = findViewById(R.id.recyclerFollowers);
        llEmptyList = findViewById(R.id.llEmptyList);

        // Configure RecyclerView
        recyclerFollowers.setLayoutManager(new LinearLayoutManager(this));

        // Set back button click listener
        btnBack.setOnClickListener(v -> finish());

        // Set up search functionality
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBlockedUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupAdapter() {
        adapter = new BlockAdapter(this, currentUserId);
        recyclerFollowers.setAdapter(adapter);
        
        // Set up click listeners for adapter
        adapter.setOnBlockedUserClickListener(blockedUser -> {
            // Navigate to user profile or show details
            Toast.makeText(this, "Xem thông tin " + blockedUser.getFullname(), Toast.LENGTH_SHORT).show();
        });
        
        adapter.setOnUnblockButtonClickListener(blockedUser -> {
            // Unblock the user
            chanTaiKhoanVM.unblockUser(currentUserId, blockedUser.getId());
            Toast.makeText(this, "Đã bỏ chặn " + blockedUser.getFullname(), Toast.LENGTH_SHORT).show();
            
            // Remove from the list
            adapter.removeUser(blockedUser.getId());
            allBlockedUsers.removeIf(user -> user.getId() == blockedUser.getId());
            
            // Update empty state
            updateEmptyState();
        });
        
        // Load blocked users
        loadBlockedUsers();
    }

    private void filterBlockedUsers(String query) {
        if (query.isEmpty()) {
            adapter.setBlockedUsers(allBlockedUsers);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            List<TaikhoanEntity> filteredList = allBlockedUsers.stream()
                    .filter(user -> 
                            user.getFullname().toLowerCase().contains(lowerCaseQuery) || 
                            user.getUsername().toLowerCase().contains(lowerCaseQuery))
                    .collect(Collectors.toList());
            adapter.setBlockedUsers(filteredList);
        }
        
        updateEmptyState();
    }
    
    private void loadBlockedUsers() {
        // Load users that the current user has blocked
        chanTaiKhoanVM.getBlockedUserIds(currentUserId).observe(this, blockedIds -> {
            if (blockedIds != null && !blockedIds.isEmpty()) {
                // Load user details for each blocked ID
                taikhoanVM.getUsersByIds(blockedIds).observe(this, blockedUsers -> {
                    allBlockedUsers = blockedUsers;
                    adapter.setBlockedUsers(blockedUsers);
                    updateEmptyState();
                });
            } else {
                allBlockedUsers.clear();
                adapter.setBlockedUsers(allBlockedUsers);
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
    
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
                setupAdapter();
            });
        }
    }
}