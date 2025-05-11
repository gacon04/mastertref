package com.example.mastertref.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mastertref.data.local.AppDatabase;
import com.example.mastertref.data.local.DaXemGanDayDAO;
import com.example.mastertref.data.local.DaXemGanDayEntity;
import com.example.mastertref.data.local.MonAnWithChiTiet;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DaXemGanDayRepository {
    private DaXemGanDayDAO daXemGanDayDAO;

    public DaXemGanDayRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        daXemGanDayDAO = database.daXemGanDayDAO();
    }

    public List<DaXemGanDayEntity> getRecentViews(int userId) {
        try {
            return new GetRecentViewsAsyncTask(daXemGanDayDAO).execute(userId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // New method to get recent views with details as LiveData
    public LiveData<List<MonAnWithChiTiet>> getRecentViewsWithDetails(int userId) {
        MutableLiveData<List<MonAnWithChiTiet>> data = new MutableLiveData<>();
        new GetRecentViewsWithDetailsAsyncTask(daXemGanDayDAO, data).execute(userId);
        return data;
    }

    public void insertRecentView(DaXemGanDayEntity entity) {
        new InsertRecentViewAsyncTask(daXemGanDayDAO).execute(entity);
    }

    public void deleteRecentViewById(int id) {
        new DeleteRecentViewByIdAsyncTask(daXemGanDayDAO).execute(id);
    }

    public void deleteAllRecentViewsByUser(int userId) {
        new DeleteAllRecentViewsByUserAsyncTask(daXemGanDayDAO).execute(userId);
    }
    
    public DaXemGanDayEntity findByUserAndMonAn(int userId, int monAnId) {
        try {
            return new FindByUserAndMonAnAsyncTask(daXemGanDayDAO).execute(userId, monAnId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetRecentViewsAsyncTask extends AsyncTask<Integer, Void, List<DaXemGanDayEntity>> {
        private DaXemGanDayDAO daXemGanDayDAO;

        private GetRecentViewsAsyncTask(DaXemGanDayDAO daXemGanDayDAO) {
            this.daXemGanDayDAO = daXemGanDayDAO;
        }

        @Override
        protected List<DaXemGanDayEntity> doInBackground(Integer... userIds) {
            return daXemGanDayDAO.getRecentViews(userIds[0]);
        }
    }
    
    private static class GetRecentViewsWithDetailsAsyncTask extends AsyncTask<Integer, Void, List<MonAnWithChiTiet>> {
        private DaXemGanDayDAO daXemGanDayDAO;
        private MutableLiveData<List<MonAnWithChiTiet>> data;

        private GetRecentViewsWithDetailsAsyncTask(DaXemGanDayDAO daXemGanDayDAO, MutableLiveData<List<MonAnWithChiTiet>> data) {
            this.daXemGanDayDAO = daXemGanDayDAO;
            this.data = data;
        }

        @Override
        protected List<MonAnWithChiTiet> doInBackground(Integer... userIds) {
            return daXemGanDayDAO.getRecentViewsWithDetails(userIds[0]);
        }
        
        @Override
        protected void onPostExecute(List<MonAnWithChiTiet> result) {
            data.postValue(result);
        }
    }

    private static class InsertRecentViewAsyncTask extends AsyncTask<DaXemGanDayEntity, Void, Void> {
        private DaXemGanDayDAO daXemGanDayDAO;

        private InsertRecentViewAsyncTask(DaXemGanDayDAO daXemGanDayDAO) {
            this.daXemGanDayDAO = daXemGanDayDAO;
        }

        @Override
        protected Void doInBackground(DaXemGanDayEntity... entities) {
            daXemGanDayDAO.insert(entities[0]);
            return null;
        }
    }

    private static class DeleteRecentViewByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private DaXemGanDayDAO daXemGanDayDAO;

        private DeleteRecentViewByIdAsyncTask(DaXemGanDayDAO daXemGanDayDAO) {
            this.daXemGanDayDAO = daXemGanDayDAO;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            daXemGanDayDAO.deleteById(ids[0]);
            return null;
        }
    }

    private static class DeleteAllRecentViewsByUserAsyncTask extends AsyncTask<Integer, Void, Void> {
        private DaXemGanDayDAO daXemGanDayDAO;

        private DeleteAllRecentViewsByUserAsyncTask(DaXemGanDayDAO daXemGanDayDAO) {
            this.daXemGanDayDAO = daXemGanDayDAO;
        }

        @Override
        protected Void doInBackground(Integer... userIds) {
            daXemGanDayDAO.deleteAllByUserId(userIds[0]);
            return null;
        }
    }
    
    private static class FindByUserAndMonAnAsyncTask extends AsyncTask<Integer, Void, DaXemGanDayEntity> {
        private DaXemGanDayDAO daXemGanDayDAO;

        private FindByUserAndMonAnAsyncTask(DaXemGanDayDAO daXemGanDayDAO) {
            this.daXemGanDayDAO = daXemGanDayDAO;
        }

        @Override
        protected DaXemGanDayEntity doInBackground(Integer... params) {
            return daXemGanDayDAO.findByUserAndMonAn(params[0], params[1]);
        }
    }
}
