package com.example.mastertref.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.MonAnVM;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;

public class ShowAllRecipe extends AppCompatActivity {
    private RecyclerView rvAllRecipes;
    private RecipeAdapter adapter;
    private MonAnVM viewModel;
    private SessionManager sessionManager;
    private String username;
    
    // UI elements
    private ImageButton btnBack, btnSearch, btnBackSearch;
    private TextView tvTitle;
    private EditText etSearch;
    private ConstraintLayout toolbar, searchLayout;
    
    // Data
    private List<MonAnWithChiTiet> allRecipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_recipe);
        
        // Initialize views
        initViews();
        
        // Set up click listeners
        setupClickListeners();
        
        // Get username from intent or session
        sessionManager = new SessionManager(this);
        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = sessionManager.getUsername();
        }
        
        // Initialize adapter and RecyclerView
        setupRecyclerView();
        
        // Initialize ViewModel and load data
        viewModel = new ViewModelProvider(this).get(MonAnVM.class);
        loadRecipes();
    }
    
    private void initViews() {
        rvAllRecipes = findViewById(R.id.rvAllRecipes);
        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        btnBackSearch = findViewById(R.id.btnBackSearch);
        tvTitle = findViewById(R.id.tvTitle);
        etSearch = findViewById(R.id.etSearch);
        toolbar = findViewById(R.id.toolbar);
        searchLayout = findViewById(R.id.searchLayout);
    }
    
    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());
        
        // Search button
        btnSearch.setOnClickListener(v -> {
            toolbar.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
        });
        
        // Back from search
        btnBackSearch.setOnClickListener(v -> {
            toolbar.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            etSearch.setText("");
            adapter.setData(allRecipes); // Reset to show all recipes
        });
        
        // Search text change listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString());
            }
            
            @Override
            public void afterTextChanged(Editable s) {}
        });
        
        // Handle search action on keyboard
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterRecipes(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }
    
    private void setupRecyclerView() {
        rvAllRecipes.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RecipeAdapter();
        rvAllRecipes.setAdapter(adapter);
    }
    
    private void loadRecipes() {
        viewModel.getMonAnWithChiTietByUsername(username)
                .observe(this, monAnList -> {
                    if (monAnList != null) {
                        allRecipes = monAnList;
                        adapter.setData(monAnList);
                    }
                });
    }
    
    private void filterRecipes(String query) {
        if (query.isEmpty()) {
            adapter.setData(allRecipes);
            return;
        }
        
        List<MonAnWithChiTiet> filteredList = new ArrayList<>();
        for (MonAnWithChiTiet recipe : allRecipes) {
            if (recipe.getMonAn().getTenMonAn().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(recipe);
            }
        }
        
        adapter.setData(filteredList);
    }
    
    // Custom adapter for recipes
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
        private List<MonAnWithChiTiet> recipeList = new ArrayList<>();
        
        public void setData(List<MonAnWithChiTiet> data) {
            recipeList.clear();
            recipeList.addAll(data);
            notifyDataSetChanged();
        }
        
        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ShowAllRecipe.this).inflate(R.layout.item_showallmon, parent, false);
            return new RecipeViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            MonAnWithChiTiet monAnChiTiet = recipeList.get(position);
            MonAnEntity monAn = monAnChiTiet.getMonAn();
            
            holder.tvTitle.setText(monAn.getTenMonAn());
            holder.tvTime.setText(monAn.getThoiGian());
            
            Glide.with(ShowAllRecipe.this)
                .load(monAn.getHinhAnh())
                .placeholder(R.drawable.mastertref)
                .error(R.drawable.mastertref)
                .into(holder.imgRecipe);
            
            // In the RecipeAdapter class, onBindViewHolder method
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ShowAllRecipe.this, ChiTietMonAnActivity.class);
                intent.putExtra("mon_an_id", monAn.getId());
                startActivity(intent);
            });
        }
        
        @Override
        public int getItemCount() {
            return recipeList.size();
        }
        
        class RecipeViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvTime;
            ImageView imgRecipe;
            
            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvRecipeTitle);
                tvTime = itemView.findViewById(R.id.tvAuthorName);
                imgRecipe = itemView.findViewById(R.id.imgRecipe);
            }
        }
    }
}