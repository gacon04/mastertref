package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mastertref.data.repository.FollowerRepository;
import com.example.mastertref.data.repository.FollowingRepository;

import java.util.List;

public class TheoDoiVM extends AndroidViewModel {
    private FollowerRepository followerRepository;
    private FollowingRepository followingRepository;
    private MutableLiveData<Boolean> followStatus = new MutableLiveData<>();
    
    public TheoDoiVM(@NonNull Application application) {
        super(application);
        followerRepository = new FollowerRepository(application);
        followingRepository = new FollowingRepository(application);
    }
    
    // Methods for following operations
    public LiveData<List<Integer>> getFollowingUserIds(int userId) {
        return followingRepository.getFollowingUserIds(userId);
    }
    
    public void followUser(int followerId, int followingId) {
        followingRepository.followUser(followerId, followingId);
        followStatus.setValue(true);
    }
    
    public void unfollowUser(int followerId, int followingId) {
        followingRepository.unfollowUser(followerId, followingId);
        followStatus.setValue(false);
    }
    
    public void checkFollowStatus(int followerId, int followingId) {
        followingRepository.isFollowing(followerId, followingId, isFollowing -> {
            followStatus.setValue(isFollowing);
        });
    }
    
    public LiveData<Boolean> getFollowStatus() {
        return followStatus;
    }
    
    // Methods for follower operations
    public LiveData<List<Integer>> getFollowerUserIds(int userId) {
        return followerRepository.getFollowerUserIds(userId);
    }
    
    // Toggle follow status
    public void toggleFollowStatus(int followerId, int followingId) {
        if (followStatus.getValue() != null && followStatus.getValue()) {
            unfollowUser(followerId, followingId);
        } else {
            followUser(followerId, followingId);
        }
    }
}
