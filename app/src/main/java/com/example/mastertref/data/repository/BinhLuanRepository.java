package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.example.mastertref.data.local.AppDatabase;

import com.example.mastertref.data.local.BinhLuanDAO;
import com.example.mastertref.data.local.BinhLuanEntity;
import com.example.mastertref.data.local.BinhLuanTaiKhoanEntity;

import java.util.List;

public class BinhLuanRepository {
    private BinhLuanDAO binhLuanDAO;
    private LiveData<List<BinhLuanEntity>> allBinhLuans;

    public BinhLuanRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        binhLuanDAO = database.binhLuanDAO();
        allBinhLuans = binhLuanDAO.getAllBinhLuans();
    }

    public LiveData<List<BinhLuanEntity>> getAllBinhLuans() {
        return allBinhLuans;
    }

    public LiveData<List<BinhLuanTaiKhoanEntity>> getAllBinhLuansWithUser() {
        return binhLuanDAO.getAllBinhLuansWithUser();
    }

    public LiveData<List<BinhLuanTaiKhoanEntity>> getBinhLuansWithUserByMonId(int monId) {
        return binhLuanDAO.getBinhLuansWithUserByMonId(monId);
    }

    public LiveData<List<BinhLuanEntity>> getBinhLuansByMonId(int monId) {
        return binhLuanDAO.getBinhLuansByMonId(monId);
    }

    public LiveData<List<BinhLuanEntity>> getBinhLuansByUserId(int userId) {
        return binhLuanDAO.getBinhLuansByUserId(userId);
    }

    public LiveData<BinhLuanEntity> getBinhLuanById(int id) {
        return binhLuanDAO.getBinhLuanById(id);
    }

    public void insertBinhLuan(BinhLuanEntity binhLuan) {
        new InsertBinhLuanAsyncTask(binhLuanDAO).execute(binhLuan);
    }

    public void updateBinhLuan(BinhLuanEntity binhLuan) {
        new UpdateBinhLuanAsyncTask(binhLuanDAO).execute(binhLuan);
    }

    public void deleteBinhLuan(BinhLuanEntity binhLuan) {
        new DeleteBinhLuanAsyncTask(binhLuanDAO).execute(binhLuan);
    }

    private static class InsertBinhLuanAsyncTask extends AsyncTask<BinhLuanEntity, Void, Void> {
        private BinhLuanDAO binhLuanDAO;

        private InsertBinhLuanAsyncTask(BinhLuanDAO binhLuanDAO) {
            this.binhLuanDAO = binhLuanDAO;
        }

        @Override
        protected Void doInBackground(BinhLuanEntity... binhLuans) {
            binhLuanDAO.insertBinhLuan(binhLuans[0]);
            return null;
        }
    }

    private static class UpdateBinhLuanAsyncTask extends AsyncTask<BinhLuanEntity, Void, Void> {
        private BinhLuanDAO binhLuanDAO;

        private UpdateBinhLuanAsyncTask(BinhLuanDAO binhLuanDAO) {
            this.binhLuanDAO = binhLuanDAO;
        }

        @Override
        protected Void doInBackground(BinhLuanEntity... binhLuans) {
            binhLuanDAO.updateBinhLuan(binhLuans[0]);
            return null;
        }
    }

    private static class DeleteBinhLuanAsyncTask extends AsyncTask<BinhLuanEntity, Void, Void> {
        private BinhLuanDAO binhLuanDAO;

        private DeleteBinhLuanAsyncTask(BinhLuanDAO binhLuanDAO) {
            this.binhLuanDAO = binhLuanDAO;
        }

        @Override
        protected Void doInBackground(BinhLuanEntity... binhLuans) {
            binhLuanDAO.deleteBinhLuan(binhLuans[0]);
            return null;
        }
    }
}