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

}
