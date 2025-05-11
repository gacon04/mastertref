package com.example.mastertref.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mastertref.R;
import com.example.mastertref.data.local.Adapter.LichSuTimKiemAdapter;
import com.example.mastertref.utils.SessionManager;
import com.example.mastertref.viewmodel.LichSuTimKiemVM;
import com.example.mastertref.viewmodel.TaikhoanVM;


public class SearchDetailsFragment extends Fragment {

    private TextView btnThoat;
    private EditText edtSearch;
    private RecyclerView rvRecentSearch;
    private TaikhoanVM taikhoanVM;
    private LichSuTimKiemVM lichSuTimKiemVM;
    private SessionManager sessionManager;
    private int currentUserId;
    private LichSuTimKiemAdapter recentSearchesAdapter;
    private CardView emptyStateView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ViewModel
        sessionManager = new SessionManager(requireContext());
        taikhoanVM = new ViewModelProvider(this).get(TaikhoanVM.class);
        lichSuTimKiemVM = new ViewModelProvider(this).get(LichSuTimKiemVM.class);
        // Initialize current user ID
        initCurrentUserId();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_details, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnThoat = view.findViewById(R.id.btnThoat);
        edtSearch = view.findViewById(R.id.edtSearch);
        rvRecentSearch = view.findViewById(R.id.recent_searches_recycler);
        emptyStateView = view.findViewById(R.id.empty_recent_searches);
        
        // Check if we have a search query from arguments
        Bundle args = getArguments();
        if (args != null && args.containsKey("search_query")) {
            String searchQuery = args.getString("search_query", "");
            edtSearch.setText(searchQuery);
            // Position cursor at the end of text
            edtSearch.setSelection(searchQuery.length());
        }
        
        setupListeners();
        setupRecentSearchesRecyclerView();
    }

    private void setupListeners() {
        // Thêm TextWatcher để theo dõi thay đổi trong edtSearch
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu có text, đổi btnThoat thành "x"
                if (s.length() > 0) {
                    btnThoat.setText("x");
                } else {
                    btnThoat.setText("Thoát");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý sự kiện click cho btnThoat
        btnThoat.setOnClickListener(v -> {
            // Nếu đang hiển thị "x" (có text trong edtSearch)
            if (btnThoat.getText().toString().equals("x")) {
                edtSearch.setText("");
            } else {
                // Nếu không có text, quay lại Fragment trước đó
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        
        // Thêm sự kiện xử lý khi người dùng bấm nút tìm kiếm trên bàn phím
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || 
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                String searchQuery = edtSearch.getText().toString().trim();
                if (!searchQuery.isEmpty()) {



                    // Tạo bundle để truyền từ khóa tìm kiếm
                    Bundle args = new Bundle();
                    args.putString("search_query", searchQuery);
                    
                    // Tạo fragment mới và truyền bundle
                    SearchResultFragment searchResultFragment = new SearchResultFragment();
                    searchResultFragment.setArguments(args);
                    
                    // Chuyển đến SearchResultFragment
                    requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, searchResultFragment)
                        .addToBackStack(null)
                        .commit();
                }
                return true;
            }
            return false;
        });
    }
    
    // In your onViewCreated method or wherever you set up your RecyclerView adapter
    private void setupRecentSearchesRecyclerView() {
        // Initialize adapter
        recentSearchesAdapter = new LichSuTimKiemAdapter();
        rvRecentSearch.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvRecentSearch.setAdapter(recentSearchesAdapter);
        
        // Set up click listeners for adapter
        recentSearchesAdapter.setOnItemClickListener(searchQuery -> {
            // Set the search query and perform search
            edtSearch.setText(searchQuery);
            edtSearch.setSelection(searchQuery.length());
            
            // Trigger search
            edtSearch.onEditorAction(EditorInfo.IME_ACTION_SEARCH);
        });
        
        recentSearchesAdapter.setOnDeleteClickListener(id -> {
            // Delete the search history item
            lichSuTimKiemVM.deleteSearchHistoryById(id);
        });
        
        // Observe the search history data
        lichSuTimKiemVM.getSearchHistoryByUser(currentUserId, 5).observe(getViewLifecycleOwner(), searchHistory -> {
            recentSearchesAdapter.setData(searchHistory);
            
            // Show empty state if there are no recent searches
            if (searchHistory == null || searchHistory.isEmpty()) {
                emptyStateView.setVisibility(View.VISIBLE);
                rvRecentSearch.setVisibility(View.GONE);
            } else {
                emptyStateView.setVisibility(View.GONE);
                rvRecentSearch.setVisibility(View.VISIBLE);
            }
        });
    }
    
    private void initCurrentUserId() {
        String username = sessionManager.getUsername();
        if (username != null && !username.isEmpty()) {
            taikhoanVM.getUserIdByUsername(username, userId -> {
                currentUserId = userId;
            });
        }
    }
}