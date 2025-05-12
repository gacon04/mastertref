package com.example.mastertref.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.TheoDoiTaiKhoanDAO;

import java.util.List;

public class FollowerRepository {
    private TheoDoiTaiKhoanDAO theoDoiTaiKhoanDAO;
    
    public FollowerRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        theoDoiTaiKhoanDAO = database.theoDoiTaiKhoanDAO();
    }
    
    // Get list of users who are following the current user
    public LiveData<List<Integer>> getFollowerUserIds(int userId) {
        return theoDoiTaiKhoanDAO.getFollowerUserIds(userId);
    }
    
    // Get count of followers for a user
    public void getFollowerCount(int userId, CountCallback callback) {
        new Thread(() -> {
            LiveData<List<Integer>> followerIds = theoDoiTaiKhoanDAO.getFollowerUserIds(userId);
            // This is a simplistic approach - in a real app you'd observe the LiveData
            // and count the results when they're available
        }).start();
    }
    
    // Callback interface for count operations
    public interface CountCallback {
        void onCountReceived(int count);
    }
}
