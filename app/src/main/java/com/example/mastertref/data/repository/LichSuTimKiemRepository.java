package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.LichsuTimkiemDAO;
import com.example.mastertref.data.local.LichsuTimkiemEntity;

import java.util.List;

public class LichSuTimKiemRepository {
    private LichsuTimkiemDAO lichsuTimkiemDAO;

    public LichSuTimKiemRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        lichsuTimkiemDAO = database.lichSuTimKiemDAO();
    }

    public LiveData<List<LichsuTimkiemEntity>> getSearchHistoryByUser(int taikhoanId, int limit) {
        return lichsuTimkiemDAO.getSearchHistoryByUser(taikhoanId, limit);
    }

    public LiveData<List<LichsuTimkiemEntity>> searchInHistory(int taikhoanId, String query) {
        return lichsuTimkiemDAO.searchInHistory(taikhoanId, query);
    }

    public void insertSearchHistory(LichsuTimkiemEntity timKiem) {
        new InsertSearchHistoryAsyncTask(lichsuTimkiemDAO).execute(timKiem);
    }

    public void deleteSearchHistoryById(int id) {
        new DeleteSearchHistoryByIdAsyncTask(lichsuTimkiemDAO).execute(id);
    }

    public void deleteAllSearchHistoryByUser(int taikhoanId) {
        new DeleteAllSearchHistoryByUserAsyncTask(lichsuTimkiemDAO).execute(taikhoanId);
    }

    private static class InsertSearchHistoryAsyncTask extends AsyncTask<LichsuTimkiemEntity, Void, Void> {
        private LichsuTimkiemDAO lichsuTimkiemDAO;

        private InsertSearchHistoryAsyncTask(LichsuTimkiemDAO lichsuTimkiemDAO) {
            this.lichsuTimkiemDAO = lichsuTimkiemDAO;
        }

        @Override
        protected Void doInBackground(LichsuTimkiemEntity... timKiems) {
            lichsuTimkiemDAO.insert(timKiems[0]);
            return null;
        }
    }

    private static class DeleteSearchHistoryByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private LichsuTimkiemDAO lichsuTimkiemDAO;

        private DeleteSearchHistoryByIdAsyncTask(LichsuTimkiemDAO lichsuTimkiemDAO) {
            this.lichsuTimkiemDAO = lichsuTimkiemDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            lichsuTimkiemDAO.deleteSearchHistoryById(ids[0]);
            return null;
        }
    }

    private static class DeleteAllSearchHistoryByUserAsyncTask extends AsyncTask<Integer, Void, Void> {
        private LichsuTimkiemDAO lichsuTimkiemDAO;

        private DeleteAllSearchHistoryByUserAsyncTask(LichsuTimkiemDAO lichsuTimkiemDAO) {
            this.lichsuTimkiemDAO = lichsuTimkiemDAO;
        }

        @Override
        protected Void doInBackground(Integer... taikhoanIds) {
            lichsuTimkiemDAO.deleteAllSearchHistoryByUser(taikhoanIds[0]);
            return null;
        }
    }

    // Add these synchronous methods to get search history
    public List<LichsuTimkiemEntity> getSearchHistoryByUserSync(int taikhoanId, int limit) {
        try {
            return new GetSearchHistoryByUserSyncTask(lichsuTimkiemDAO).execute(taikhoanId, limit).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<LichsuTimkiemEntity> searchInHistorySync(int taikhoanId, String query) {
        try {
            return new SearchInHistorySyncTask(lichsuTimkiemDAO, query).execute(taikhoanId).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // AsyncTask for synchronous search history retrieval
    private static class GetSearchHistoryByUserSyncTask extends AsyncTask<Integer, Void, List<LichsuTimkiemEntity>> {
        private LichsuTimkiemDAO lichsuTimkiemDAO;

        private GetSearchHistoryByUserSyncTask(LichsuTimkiemDAO lichsuTimkiemDAO) {
            this.lichsuTimkiemDAO = lichsuTimkiemDAO;
        }

        @Override
        protected List<LichsuTimkiemEntity> doInBackground(Integer... params) {
            int taikhoanId = params[0];
            int limit = params[1];
            return lichsuTimkiemDAO.getSearchHistoryByUserSync(taikhoanId, limit);
        }
    }

    // AsyncTask for synchronous search in history
    private static class SearchInHistorySyncTask extends AsyncTask<Integer, Void, List<LichsuTimkiemEntity>> {
        private LichsuTimkiemDAO lichsuTimkiemDAO;
        private String query;

        private SearchInHistorySyncTask(LichsuTimkiemDAO lichsuTimkiemDAO, String query) {
            this.lichsuTimkiemDAO = lichsuTimkiemDAO;
            this.query = query;
        }

        @Override
        protected List<LichsuTimkiemEntity> doInBackground(Integer... params) {
            int taikhoanId = params[0];
            return lichsuTimkiemDAO.searchInHistorySync(taikhoanId, query);
        }
    }
}
