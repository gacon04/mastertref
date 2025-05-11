package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.DaXemGanDayEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;
import com.example.mastertref.data.repository.DaXemGanDayRepository;

import java.util.List;

public class DaXemGanDayVM extends AndroidViewModel {
    private DaXemGanDayRepository repository;

    public DaXemGanDayVM(@NonNull Application application) {
        super(application);
        repository = new DaXemGanDayRepository(application);
    }

    public List<DaXemGanDayEntity> getRecentViews(int userId) {
        return repository.getRecentViews(userId);
    }
    
    // New method to get recent views with details as LiveData
    public LiveData<List<MonAnWithChiTiet>> getRecentViewsWithDetails(int userId) {
        return repository.getRecentViewsWithDetails(userId);
    }

    public void insertRecentView(DaXemGanDayEntity entity) {
        repository.insertRecentView(entity);
    }

    public void deleteRecentViewById(int id) {
        repository.deleteRecentViewById(id);
    }

    public void deleteAllRecentViewsByUser(int userId) {
        repository.deleteAllRecentViewsByUser(userId);
    }

    // Helper method to create and insert a new recent view entry
    public void addRecentView(int userId, int monAnId) {
        DaXemGanDayEntity entity = new DaXemGanDayEntity(userId, monAnId, System.currentTimeMillis());
        insertRecentView(entity);
    }

    // Method to save a unique recent view
    public void saveUniqueRecentView(int userId, int monAnId) {
        // This should be executed on a background thread
        new Thread(() -> {
            // Get the most recent views
            List<DaXemGanDayEntity> recentViews = getRecentViews(userId);
            
            // Check if this food item is already in the recent views
            DaXemGanDayEntity existingView = repository.findByUserAndMonAn(userId, monAnId);
            
            if (existingView != null) {
                // If it exists but is not the most recent one, delete it and add a new one
                if (recentViews != null && !recentViews.isEmpty() && 
                    recentViews.get(0).getMonAnId() != monAnId) {
                    // Delete the existing view
                    deleteRecentViewById(existingView.getId());
                    
                    // Add a new view with current timestamp
                    addRecentView(userId, monAnId);
                }
                // If it's already the most recent one, do nothing
            } else {
                // If it doesn't exist in recent views, add it
                addRecentView(userId, monAnId);
            }
        }).start();
    }
}
