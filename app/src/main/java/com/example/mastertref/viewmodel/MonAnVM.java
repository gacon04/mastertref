package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.repository.MonAnRepository;

import java.util.List;

public class MonAnVM extends AndroidViewModel {
    private MonAnRepository repository;
    private LiveData<List<MonAnWithChiTiet>> monAnWithChiTietList;
    public MonAnVM(@NonNull Application application) {
        super(application);
        repository = new MonAnRepository(application);
    }

    public LiveData<List<MonAnEntity>> getMonAnByUsername(String username) {
        return repository.getMonAnByUsername(username);
    }
    public LiveData<List<MonAnWithChiTiet>> getMonAnWithChiTietByUsername(String username) {
        return repository.getMonAnWithChiTietByUsername(username);
    }
    public LiveData<MonAnWithChiTiet> getMonAnWithChiTietById(int id) {
        return repository.getMonAnWithChiTietById(id);
    }
    // lay mon an goi y
    public LiveData<List<MonAnWithChiTiet>> getRecommendedRecipes(int sourceMonAnId, int limit) {
        return repository.getRecommendedRecipes(sourceMonAnId, limit);
    }
    public LiveData<List<MonAnWithChiTiet>> getRandomRecipesExcept(int sourceMonAnId, int limit) {
        return repository.getRandomRecipesExcept(sourceMonAnId, limit);
    }

    // Phương thức tìm kiếm món ăn dựa trên từ khóa
    // Add this method to your MonAnVM class
    
    public LiveData<List<MonAnWithChiTiet>> searchRecipesByNameOrIngredient(String query, int currentUserId) {
        return repository.searchMonAnByNameOrIngredient(query, currentUserId);
    }
    
    // Phương thức lấy các món ăn mới nhất (đã lọc người dùng bị chặn)
    public LiveData<List<MonAnWithChiTiet>> getNewestRecipesFiltered(int currentUserId, int limit) {
        return repository.getNewestRecipesFiltered(currentUserId, limit);
    }
}
