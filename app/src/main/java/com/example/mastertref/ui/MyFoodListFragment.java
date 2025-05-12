package com.example.mastertref.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.AlbumAdapter;
import com.example.mastertref.data.local.AlbumWithMonAn;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.AlbumVM;
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyFoodListFragment extends Fragment {
    private SessionManager sessionManager;
    private TaikhoanVM taikhoanVM;
    private AlbumVM albumViewModel;
    private AlbumAdapter albumAdapter;
    private int currentUserId, showingUserId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        albumViewModel = new ViewModelProvider(this).get(AlbumVM.class);

        initCurrentUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_food_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupClickListeners(view);
        observeAlbums();
    }

    private void setupClickListeners(View view) {
        // Add a FloatingActionButton for creating new albums
        Button fabAddAlbum = view.findViewById(R.id.btnAdd);
        if (fabAddAlbum != null) {
            fabAddAlbum.setOnClickListener(v -> {
                // Show dialog to create new album
                showCreateAlbumDialog();
            });
        }
    }
    
    private void initViews(View view) {
        // Initialize UI components
        RecyclerView rvAlbum = view.findViewById(R.id.recyclerCollections);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
        rvAlbum.setLayoutManager(gridLayoutManager);
        
        // Initialize adapter
        albumAdapter = new AlbumAdapter(requireContext());
        rvAlbum.setAdapter(albumAdapter);
        
        // Set click listener for albums
        albumAdapter.setOnAlbumClickListener(album -> {
            // Navigate to album detail screen
            navigateToAlbumDetail(album);
        });
    }
    
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
                showingUserId = userId; // Default to showing current user's albums
                // Load albums after getting user ID
                if (albumViewModel != null) {
                    albumViewModel.loadAlbumsForUser(userId);
                }
            });
        }
    }
    
    private void observeAlbums() {
        albumViewModel.getAlbumsWithMonAn().observe(getViewLifecycleOwner(), albums -> {
            if (albums != null) {
                albumAdapter.setAlbums(albums);
            }
        });
    }
    
    private void showCreateAlbumDialog() {
        // Implement dialog for creating new album
        // This is a placeholder - you'll need to implement the actual dialog
        // For example, using AlertDialog with an EditText for the album name
    }
    
    private void navigateToAlbumDetail(AlbumWithMonAn album) {
        // Navigate to album detail screen
        // This is a placeholder - you'll need to implement the actual navigation
    }
}