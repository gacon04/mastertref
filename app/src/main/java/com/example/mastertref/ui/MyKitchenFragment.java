package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.MonAnAdapter;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;
import com.example.mastertref.viewmodel.TaikhoanVM;
import com.example.mastertref.viewmodel.TheoDoiVM;

public class MyKitchenFragment extends Fragment {
    private TaikhoanVM taikhoanVM;
    private SessionManager sessionManager;
    private TheoDoiVM theoDoiVM; // Add TheoDoiVM

    private TextView tvHoten, tvUsername, tv_followers, tv_following;
    private Button btnShowAllRecipe;
    private ImageView ivAvatar;
    private LinearLayout llEmptyRecipe,llRecipe;
    private String username;
    private int userId; // Add userId to store the current user's ID
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo SessionManager và TaikhoanVM
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        theoDoiVM = new ViewModelProvider(this).get(TheoDoiVM.class); // Initialize TheoDoiVM
        
        return inflater.inflate(R.layout.fragment_my_kitchen, container, false);
    }
    private MonAnAdapter adapter;
    private MonAnVM viewModel;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo views
        tvHoten = view.findViewById(R.id.tv_hoten);
        tv_followers  = view.findViewById(R.id.tv_followers);
        tv_following = view.findViewById(R.id.tv_following);
        tvUsername = view.findViewById(R.id.tv_username);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        btnShowAllRecipe = view.findViewById(R.id.btnViewAllRecipes);
        llEmptyRecipe = view.findViewById(R.id.llEmptyRecipe);
        llRecipe = view.findViewById(R.id.llRecipe);
        // Load dữ liệu
        loadUserData();
        Button suaThongTinButton = view.findViewById(R.id.btn_edit_info_button);

        // Initialize the "Show All" button as GONE by default
        btnShowAllRecipe.setVisibility(View.GONE);
        
        // Set up "Show All" button click listener
        btnShowAllRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ShowAllRecipe.class);
            startActivity(intent);

        });
        tv_followers.setOnClickListener(v -> {
            if (userId > 0) {
                // Navigate to FollowersFragment
                FollowersFragment fragment = new FollowersFragment();
                Bundle args = new Bundle();
                args.putInt("user_id", userId);
                fragment.setArguments(args);
                
                requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
            }
        });
        
        tv_following.setOnClickListener(v -> {
            if (userId > 0) {
                // Navigate to FollowingFragment
                FollowingFragment fragment = new FollowingFragment();
                Bundle args = new Bundle();
                args.putInt("user_id", userId);
                fragment.setArguments(args);
                
                requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .addToBackStack(null)
                    .commit();
            }
        });

        adapter = new MonAnAdapter(requireContext());
        adapter.setItemLimit(2);
        RecyclerView recyclerView = view.findViewById(R.id.rvMonAn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MonAnVM.class);
        viewModel.getMonAnWithChiTietByUsername(sessionManager.getUsername())
                .observe(getViewLifecycleOwner(), monAnWithChiTietList -> {
                    adapter.setData(monAnWithChiTietList);

                    // Show or hide the "Show All" button based on data size
                    if (adapter.hasMoreItems()) {
                        btnShowAllRecipe.setVisibility(View.VISIBLE);
                    } else {
                        btnShowAllRecipe.setVisibility(View.GONE);
                    }
                    
                    // Show llEmptyRecipe if there are no recipes, otherwise show llRecipe
                    if (monAnWithChiTietList == null || monAnWithChiTietList.isEmpty()) {
                        llEmptyRecipe.setVisibility(View.VISIBLE);
                        llRecipe.setVisibility(View.GONE);
                    } else {
                        llEmptyRecipe.setVisibility(View.GONE);
                        llRecipe.setVisibility(View.VISIBLE);
                    }
                });

        adapter.setOnItemClickListener(monAnChiTiet -> {
            if (monAnChiTiet != null && monAnChiTiet.getMonAn() != null) {
                Intent intent = new Intent(requireContext(), ChiTietMonAnActivity.class);
                intent.putExtra("mon_an_id", monAnChiTiet.getMonAn().getId());
                startActivity(intent);
            } else {
                Toast.makeText(requireContext(), "Không thể mở chi tiết món ăn", Toast.LENGTH_SHORT).show();
            }
        });



        // Bắt sự kiện click và mở Activity mới
        suaThongTinButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangeMyInfo.class);
            startActivity(intent);
        });


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
                tvHoten.setText(user.getFullname());
                tvUsername.setText("@" + user.getUsername());
                userId = user.getId(); // Store the user ID
                
                // Load follower and following counts
                loadFollowerAndFollowingCounts(userId);
                
                if (user.getImageLink() != null && !user.getImageLink().isEmpty()) {
                    ImageHelper.loadImage(ivAvatar, user.getImageLink());
                }
            }
        });
    }
    
    // Add new method to load follower and following counts
    private void loadFollowerAndFollowingCounts(int userId) {
        // Load follower count
        theoDoiVM.getFollowerUserIds(userId).observe(getViewLifecycleOwner(), followerIds -> {
            int followerCount = followerIds != null ? followerIds.size() : 0;
            tv_followers.setText(followerCount + " người theo dõi");
        });
        
        // Load following count
        theoDoiVM.getFollowingUserIds(userId).observe(getViewLifecycleOwner(), followingIds -> {
            int followingCount = followingIds != null ? followingIds.size() : 0;
            tv_following.setText(followingCount + " đang theo dõi");
        });
    }
}
