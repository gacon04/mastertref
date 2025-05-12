package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.TheoDoiTaiKhoanDAO;
import com.example.mastertref.data.local.TheoDoiTaiKhoanEntity;

import java.util.List;

public class FollowingRepository {
    private TheoDoiTaiKhoanDAO theoDoiTaiKhoanDAO;
    
    public FollowingRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        theoDoiTaiKhoanDAO = database.theoDoiTaiKhoanDAO();
    }
    
    // Get list of users that the current user is following
    public LiveData<List<Integer>> getFollowingUserIds(int userId) {
        return theoDoiTaiKhoanDAO.getFollowingUserIds(userId);
    }
    
    // Follow a user
    public void followUser(int followerId, int followingId) {
        long currentTime = System.currentTimeMillis();
        TheoDoiTaiKhoanEntity theoDoiEntity = new TheoDoiTaiKhoanEntity(followerId, followingId, currentTime);
        new InsertFollowingAsyncTask(theoDoiTaiKhoanDAO).execute(theoDoiEntity);
    }
    
    // Unfollow a user
    public void unfollowUser(int followerId, int followingId) {
        new UnfollowAsyncTask(theoDoiTaiKhoanDAO).execute(followerId, followingId);
    }
    
    // Check if user is following another user
    public void isFollowing(int followerId, int followingId, FollowingCheckCallback callback) {
        new IsFollowingAsyncTask(theoDoiTaiKhoanDAO, callback).execute(followerId, followingId);
    }
    
    // AsyncTask classes for database operations
    private static class InsertFollowingAsyncTask extends AsyncTask<TheoDoiTaiKhoanEntity, Void, Void> {
        private TheoDoiTaiKhoanDAO dao;
        
        InsertFollowingAsyncTask(TheoDoiTaiKhoanDAO dao) {
            this.dao = dao;
        }
        
        @Override
        protected Void doInBackground(TheoDoiTaiKhoanEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }
    
    private static class UnfollowAsyncTask extends AsyncTask<Integer, Void, Void> {
        private TheoDoiTaiKhoanDAO dao;
        
        UnfollowAsyncTask(TheoDoiTaiKhoanDAO dao) {
            this.dao = dao;
        }
        
        @Override
        protected Void doInBackground(Integer... integers) {
            dao.unfollow(integers[0], integers[1]);
            return null;
        }
    }
    
    private static class IsFollowingAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private TheoDoiTaiKhoanDAO dao;
        private FollowingCheckCallback callback;
        
        IsFollowingAsyncTask(TheoDoiTaiKhoanDAO dao, FollowingCheckCallback callback) {
            this.dao = dao;
            this.callback = callback;
        }
        
        @Override
        protected Boolean doInBackground(Integer... integers) {
            int count = dao.isFollowing(integers[0], integers[1]);
            return count > 0;
        }
        
        @Override
        protected void onPostExecute(Boolean isFollowing) {
            callback.onResult(isFollowing);
        }
    }
    
    // Callback interface for isFollowing check
    public interface FollowingCheckCallback {
        void onResult(boolean isFollowing);
    }
}
