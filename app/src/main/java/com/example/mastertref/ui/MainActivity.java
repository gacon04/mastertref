package com.example.mastertref.ui;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mastertref.R;
import com.example.mastertref.databinding.MainBinding;
import com.example.mastertref.utils.CloudinaryHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    MainBinding binding;
    FloatingActionButton fab;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            // Kiểm tra xem intent có action OPEN_USER_PROFILE không
            if ("OPEN_USER_PROFILE".equals(intent.getAction())) {
                int userId = intent.getIntExtra("user_id", -1);
                if (userId != -1) {
                    // Tạo bundle để truyền dữ liệu cho fragment
                    Bundle args = new Bundle();
                    args.putInt("user_id", userId);
                    
                    // Tạo fragment mới và truyền bundle
                    ShowOthersPro5Fragment fragment = new ShowOthersPro5Fragment();
                    fragment.setArguments(args);
                    
                    // Thay thế fragment hiện tại bằng ShowOthersPro5Fragment
                    getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloudinaryHelper.init(this);
        binding = MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Xử lý intent trước khi thay thế fragment mặc định
        if (getIntent() != null && "OPEN_USER_PROFILE".equals(getIntent().getAction())) {
            handleIntent(getIntent());
        } else {
            replaceFragment(new SearchFragment());
        }

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (item.getItemId() == R.id.my_list) {
                replaceFragment(new MyFoodListFragment());
            } else if (item.getItemId() == R.id.my_kitchen) {
                replaceFragment(new MyKitchenFragment());
            } else if (item.getItemId() == R.id.setting) {
                replaceFragment(new SettingFragment());
            }


            return true;
        });
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}