package com.example.mastertref.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.ChanTaiKhoanDAO;
import com.example.mastertref.data.local.ChanTaiKhoanEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BlockRepository {
    private ChanTaiKhoanDAO chanTaiKhoanDAO;
    private LiveData<List<Integer>> blockedUserIds;
    private ExecutorService executorService;

    public BlockRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        chanTaiKhoanDAO = db.chanTaiKhoanDAO();
        executorService = Executors.newSingleThreadExecutor();
    }

    // Get all user IDs that the current user has blocked
    public LiveData<List<Integer>> getBlockedUserIds(int blockerId) {
        return chanTaiKhoanDAO.getBlockedUserIds(blockerId);
    }

    // Block a user
    public void blockUser(int blockerId, int blockedId) {
        executorService.execute(() -> {
            ChanTaiKhoanEntity entity = new ChanTaiKhoanEntity(blockerId, blockedId, System.currentTimeMillis());
            entity.setBlockerId(blockerId);
            entity.setBlockedId(blockedId);
            chanTaiKhoanDAO.insertBlock(entity);
        });
    }

    // Unblock a user
    public void unblockUser(int blockerId, int blockedId) {
        executorService.execute(() -> {
            ChanTaiKhoanEntity entity = new ChanTaiKhoanEntity(blockerId,blockedId,System.currentTimeMillis());
            chanTaiKhoanDAO.deleteBlock(entity);
        });
    }

    // Check if a user is blocked
    public void isBlocked(int blockerId, int blockedId, OnBooleanResultListener listener) {
        executorService.execute(() -> {
            int count = chanTaiKhoanDAO.isBlocked(blockerId, blockedId);
            boolean isBlocked = count > 0;
            listener.onResult(isBlocked);
        });
    }

    // Interface for boolean result callback
    public interface OnBooleanResultListener {
        void onResult(boolean result);
    }
}
