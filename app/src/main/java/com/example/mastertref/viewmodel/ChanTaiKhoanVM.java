package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mastertref.data.repository.BlockRepository;

import java.util.List;

public class ChanTaiKhoanVM extends AndroidViewModel {
    private BlockRepository blockRepository;
    private MutableLiveData<Boolean> blockStatus = new MutableLiveData<>();

    public ChanTaiKhoanVM(@NonNull Application application) {
        super(application);
        blockRepository = new BlockRepository(application);
    }

    // Get all user IDs that the current user has blocked
    public LiveData<List<Integer>> getBlockedUserIds(int userId) {
        return blockRepository.getBlockedUserIds(userId);
    }

    // Block a user
    public void blockUser(int blockerId, int blockedId) {
        blockRepository.blockUser(blockerId, blockedId);
        blockStatus.setValue(true);
    }

    // Unblock a user
    public void unblockUser(int blockerId, int blockedId) {
        blockRepository.unblockUser(blockerId, blockedId);
        blockStatus.setValue(false);
    }

    // Check if a user is blocked
    public void checkBlockStatus(int blockerId, int blockedId) {
        blockRepository.isBlocked(blockerId, blockedId, isBlocked -> {
            blockStatus.setValue(isBlocked);
        });
    }

    // Get block status
    public LiveData<Boolean> getBlockStatus() {
        return blockStatus;
    }
}
