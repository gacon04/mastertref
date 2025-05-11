package com.example.mastertref.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.MonAnDAO;
import com.example.mastertref.data.local.MonAnEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;

import java.util.List;

public class MonAnRepository {
    private final MonAnDAO monAnDAO;

    public MonAnRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        monAnDAO = db.monAnDAO();
    }

    public LiveData<List<MonAnEntity>> getMonAnByUsername(String username) {
        return monAnDAO.getMonAnByUsername(username);
    }
    public LiveData<List<MonAnWithChiTiet>> getMonAnWithChiTietByUsername(String username) {
        return monAnDAO.getMonAnWithChiTietByUsername(username);
    }
    public LiveData<MonAnWithChiTiet> getMonAnWithChiTietById(int id) {
        return monAnDAO.getMonAnWithChiTietById(id);
    }
    // Add this method to your repository class
    public LiveData<List<MonAnWithChiTiet>> getRecommendedRecipes(int sourceMonAnId, int limit) {
        return monAnDAO.getRecommendedRecipes(sourceMonAnId, limit);
    }
    public LiveData<List<MonAnWithChiTiet>> getRandomRecipesExcept(int sourceMonAnId, int limit) {
        return monAnDAO.getRandomRecipesExcept(sourceMonAnId, limit);
    }

    // Phương thức tìm kiếm món ăn theo tên
    public LiveData<List<MonAnWithChiTiet>> searchMonAnByName(String keyword) {
        return monAnDAO.searchMonAnByName(keyword);
    }

    // Phương thức tìm kiếm món ăn theo nguyên liệu
    public LiveData<List<MonAnWithChiTiet>> searchMonAnByIngredient(String keyword) {
        return monAnDAO.searchMonAnByIngredient(keyword);
    }

    // Phương thức tìm kiếm món ăn theo tên hoặc nguyên liệu (không lọc người dùng bị chặn)
    public LiveData<List<MonAnWithChiTiet>> searchMonAnByNameOrIngredient(String keyword) {
        return monAnDAO.searchMonAnByNameOrIngredient(keyword);
    }
    
    // Phương thức tìm kiếm món ăn theo tên hoặc nguyên liệu (có lọc người dùng bị chặn)
    public LiveData<List<MonAnWithChiTiet>> searchMonAnByNameOrIngredient(String keyword, int currentUserId) {
        return monAnDAO.searchMonAnByNameOrIngredient(keyword, currentUserId);
    }
    
    // Phương thức lấy các món ăn mới nhất (đã lọc người dùng bị chặn)
    public LiveData<List<MonAnWithChiTiet>> getNewestRecipesFiltered(int currentUserId, int limit) {
        return monAnDAO.getNewestRecipesFiltered(currentUserId, limit);
    }
}
