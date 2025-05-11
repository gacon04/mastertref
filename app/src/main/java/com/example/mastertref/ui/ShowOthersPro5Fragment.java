package com.example.mastertref.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.MonAnAdapter;
import com.example.mastertref.utils.ImageHelper;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;
import com.example.mastertref.viewmodel.TaikhoanVM;


public class ShowOthersPro5Fragment extends Fragment {

    private TextView tvCollapseName,tvFullName,tvUsername,tvFollowing, tvFollowers, tvDesciption, tvLocation;
    private ImageView imgUserProfile, ivLocation;
    private ImageButton btnMore,btnFilter;
    private Button btnFollow;
    private RecyclerView rvRecipes;
    private EditText etSearch;
    // ViewModel
    private MonAnVM monAnVM;
    private TaikhoanVM taikhoanVM;
    private MonAnAdapter adapter;
    private SessionManager sessionManager;
    private int currentUserId, profileId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        monAnVM = new ViewModelProvider(this).get(MonAnVM.class);
        initCurrentUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_others_pro5, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        Bundle args = getArguments();
        if (args != null && args.containsKey("user_id")) {
            profileId = args.getInt ("user_id", 0);
        }
        loadUserData();
        setupClickListeners();


    }
    private void blockUser(){
        PopupMenu popup = new PopupMenu(requireContext(), btnFilter);
        Menu menu = popup.getMenu();
        menu.add(Menu.NONE, 1, Menu.NONE, "Chặn người dùng");
        menu.add(Menu.NONE, 2, Menu.NONE, "Báo cáo người dùng");
    }
    private void setupClickListeners() {
        btnMore.setOnClickListener(v -> {
            blockUser();

        });
        btnFilter.setOnClickListener(v -> {
            // Handle filter button click
        });
        btnFollow.setOnClickListener(v -> {
            // Handle follow button click
        });

    }
    private void initViews(View view) {
        tvCollapseName = view.findViewById(R.id.tvCollapsedTitle);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        tvDesciption = view.findViewById(R.id.tvDesciption);
        tvLocation = view.findViewById(R.id.tvLocation);
        imgUserProfile = view.findViewById(R.id.imgUserProfile);
        ivLocation = view.findViewById(R.id.ivLocation);
        btnMore = view.findViewById(R.id.btnMore);
        btnFilter = view.findViewById(R.id.btnFilter);
        btnFollow = view.findViewById(R.id.btnFollow);
        rvRecipes = view.findViewById(R.id.rvRecipes);
        etSearch = view.findViewById(R.id.etSearch);

    }
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
            });
        }
    }
    private void loadUserData() {
        taikhoanVM.getUserById(profileId).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                // Set user's full name
                tvFullName.setText(user.getFullname());
                tvCollapseName.setText(user.getFullname());
                
                // Set username
                tvUsername.setText("@" + user.getUsername());
                
                // Load avatar image if available
                if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                    ImageHelper.loadImage(imgUserProfile, user.getAvatar());
                }
                
                // Handle description - hide if empty
                if (user.getDescription() != null && !user.getDescription().isEmpty()) {
                    tvDesciption.setText(user.getDescription());
                    tvDesciption.setVisibility(View.VISIBLE);
                } else {
                    tvDesciption.setVisibility(View.GONE);
                }
                
                // Handle location - hide the entire location section if empty
                if (user.getFrom() != null && !user.getFrom().isEmpty()) {
                    tvLocation.setText(user.getFrom());
                    tvLocation.setVisibility(View.VISIBLE);
                    ivLocation.setVisibility(View.VISIBLE);
                } else {
                    tvLocation.setVisibility(View.GONE);
                    ivLocation.setVisibility(View.GONE);
                }
                
                // You can add code here to load followers and following counts
                // For now, we'll leave the default text
            }
        });
    }

}