package com.example.mastertref.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.AlbumAdapter;
import com.example.mastertref.data.local.Adapter.FollowerAdapter;
import com.example.mastertref.data.local.AlbumWithMonAn;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.AlbumVM;
import com.example.mastertref.viewmodel.TaikhoanVM;

public class MyFoodListFragment extends Fragment {
    private static final String TAG = "MyFoodListFragment";
    private SessionManager sessionManager;
    private TaikhoanVM taikhoanVM;
    private AlbumVM albumViewModel;
    private AlbumAdapter albumAdapter;
    private int currentUserId;
    private RecyclerView rvAlbum;
    private boolean isDataLoaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        sessionManager = new SessionManager(requireContext());
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
        Log.d(TAG, "onViewCreated");

        // Initialize ViewModels
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        albumViewModel = new ViewModelProvider(this).get(AlbumVM.class);

        // Initialize views first so the adapter is ready
        initViews(view);
        setupClickListeners(view);

        // Then load user data and albums
        initCurrentUserId();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");

        // Remove observers to prevent callbacks after view is destroyed
        if (albumViewModel != null) {
            albumViewModel.getAlbumsWithMonAn().removeObservers(getViewLifecycleOwner());
        }

        // Clean up any references to views
        if (rvAlbum != null) {
            rvAlbum.setAdapter(null);
        }
        albumAdapter = null;
        rvAlbum = null;
        super.onDestroyView();
    }

    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        Log.d(TAG, "Getting user ID for username: " + username);

        if (username != null && !username.isEmpty()) {
            try {
                taikhoanVM.getUserByUsername(username).observe(getViewLifecycleOwner(), user -> {
                    if (!isAdded() || isDetached()) {
                        Log.d(TAG, "Fragment not attached, skipping user ID processing");
                        return;
                    }

                    if (user != null) {
                        currentUserId = user.getId();
                        Log.d(TAG, "Current User ID: " + currentUserId);

                        // Load albums after getting user ID
                        if (albumViewModel != null && currentUserId > 0) {
                            Log.d(TAG, "Loading albums for user ID: " + currentUserId);

                            // First clear any existing observers
                            if (albumViewModel.getAlbumsWithMonAn() != null) {
                                albumViewModel.getAlbumsWithMonAn().removeObservers(getViewLifecycleOwner());
                            }

                            // Then load albums and observe them
                            albumViewModel.loadAlbumsForUser(currentUserId);
                            observeAlbums();
                        } else {
                            Log.e(TAG, "Cannot load albums: albumViewModel is null or invalid userId");
                        }
                    } else {
                        Log.e(TAG, "User is null");
                        if (isAdded() && !isDetached()) {
                            Toast.makeText(requireContext(), "Không thể tải dữ liệu: Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error observing user data: " + e.getMessage(), e);
            }
        } else {
            // Handle case where username is not available
            Log.e(TAG, "Username is empty or null");
            if (isAdded() && !isDetached()) {
                Toast.makeText(requireContext(), "Không thể tải dữ liệu: Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void observeAlbums() {
        if (!isAdded() || isDetached() || getActivity() == null) {
            Log.d(TAG, "Fragment not attached, skipping album observation");
            return;
        }

        try {
            // Remove any existing observers to prevent duplicates
            albumViewModel.getAlbumsWithMonAn().removeObservers(getViewLifecycleOwner());

            // Use the ViewModel's LiveData to observe changes
            albumViewModel.getAlbumsWithMonAn().observe(getViewLifecycleOwner(), albums -> {
                // Check if fragment is still attached before updating UI
                if (!isAdded() || isDetached() || getActivity() == null) {
                    Log.d(TAG, "Fragment not attached, skipping album update");
                    return;
                }

                if (albums != null) {
                    Log.d(TAG, "Received " + albums.size() + " albums");

                    // Update the adapter with the new data
                    if (albumAdapter != null) {
                        Log.d(TAG, "Updating adapter with " + albums.size() + " albums");
                        albumAdapter.setAlbums(albums);
                    } else {
                        Log.e(TAG, "Album adapter is null");
                        // Try to reinitialize the adapter if it's null
                        if (rvAlbum != null && getContext() != null) {
                            albumAdapter = new AlbumAdapter(requireContext());
                            rvAlbum.setAdapter(albumAdapter);
                            albumAdapter.setAlbums(albums);
                        }
                    }

                    // If no albums, maybe create a default one
                    if (albums.isEmpty() && currentUserId > 0) {
                        Log.d(TAG, "No albums found, creating default album");
                        albumViewModel.createAlbum("Đã lưu", currentUserId);
                    }
                } else {
                    Log.e(TAG, "Received null albums list");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error observing albums: " + e.getMessage(), e);
        }
    }

    private void showCreateAlbumDialog() {
        // Create an EditText for the album name input
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Tạo album mới");

        // Set up the input
        final android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setHint("Nhập tên album");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Tạo", (dialog, which) -> {
            String albumName = input.getText().toString().trim();
            if (!albumName.isEmpty()) {
                if (currentUserId > 0) {
                    albumViewModel.createAlbum(albumName, currentUserId);
                    // Toast to confirm album creation
                    Toast.makeText(requireContext(),
                            "Đã tạo album: " + albumName,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(),
                            "Không thể tạo album: Vui lòng đăng nhập lại",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(),
                        "Tên album không được để trống",
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void navigateToAlbumDetail(AlbumWithMonAn album) {
        // For now, just show a toast with the album name and number of recipes
        String message = "Album: " + album.album.getTenAlbum() +
                " (ID: " + album.album.getId() + ", " +
                (album.monAnList != null ? album.monAnList.size() : 0) + " món ăn)";
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

        // TODO: Navigate to a detail fragment/activity
        // Bundle args = new Bundle();
        // args.putInt("albumId", album.album.getId());
        // Navigation.findNavController(requireView()).navigate(R.id.action_to_albumDetailFragment, args);
    }

    private void initViews(View view) {
        // Initialize UI components
        rvAlbum = view.findViewById(R.id.recyclerCollections);
        if (rvAlbum != null) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2); // 2 columns
            rvAlbum.setLayoutManager(gridLayoutManager);

            // Initialize adapter
            albumAdapter = new AlbumAdapter(requireContext());
            rvAlbum.setAdapter(albumAdapter);

            // Set click listener for albums
            albumAdapter.setOnAlbumClickListener(album -> {
                if (isAdded() && !isDetached()) {
                    // Navigate to album detail screen
                    navigateToAlbumDetail(album);
                }
            });
        } else {
            Log.e(TAG, "RecyclerView not found in layout");
        }
    }

    private void setupClickListeners(View view) {
        // Add button for creating new albums
        ImageButton btnAddAlbum = view.findViewById(R.id.btnAdd);
        if (btnAddAlbum != null) {
            btnAddAlbum.setOnClickListener(v -> {
                // Show dialog to create new album
                showCreateAlbumDialog();
            });
        } else {
            Log.e(TAG, "Add button not found in layout");
        }

        // You can add more click listeners for other UI elements here
    }
}