package com.example.mastertref.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mastertref.data.local.LichsuTimkiemEntity;
import com.example.mastertref.data.repository.LichSuTimKiemRepository;

import java.util.List;

public class LichSuTimKiemVM extends AndroidViewModel {
    private LichSuTimKiemRepository repository;

    public LichSuTimKiemVM(@NonNull Application application) {
        super(application);
        repository = new LichSuTimKiemRepository(application);
    }

    public LiveData<List<LichsuTimkiemEntity>> getSearchHistoryByUser(int taikhoanId, int limit) {
        return repository.getSearchHistoryByUser(taikhoanId, limit);
    }

    public LiveData<List<LichsuTimkiemEntity>> searchInHistory(int taikhoanId, String query) {
        return repository.searchInHistory(taikhoanId, query);
    }

    public void insertSearchHistory(LichsuTimkiemEntity timKiem) {
        repository.insertSearchHistory(timKiem);
    }

    public void deleteSearchHistoryById(int id) {
        repository.deleteSearchHistoryById(id);
    }

    public void deleteAllSearchHistoryByUser(int taikhoanId) {
        repository.deleteAllSearchHistoryByUser(taikhoanId);
    }

    // Helper method to create and insert a new search history entry
    public void addSearchQuery(int taikhoanId, String query) {
        LichsuTimkiemEntity timKiem = new LichsuTimkiemEntity(taikhoanId, query, System.currentTimeMillis());
        timKiem.setTaikhoanId(taikhoanId);
        timKiem.setTuKhoa(query);
        timKiem.setThoiGian(System.currentTimeMillis());
        insertSearchHistory(timKiem);
    }

    // Add this method to LichSuTimKiemVM
    public void saveUniqueSearchQuery(int taikhoanId, String query) {
        // This should be executed on a background thread
        new Thread(() -> {
            // Check if this is already the most recent search
            List<LichsuTimkiemEntity> recentSearches = repository.getSearchHistoryByUserSync(taikhoanId, 1);
            
            if (recentSearches == null || recentSearches.isEmpty() || 
                !recentSearches.get(0).getTuKhoa().equals(query)) {
                
                // Delete any existing instances of this query
                List<LichsuTimkiemEntity> existingQueries = repository.searchInHistorySync(taikhoanId, query);
                if (existingQueries != null && !existingQueries.isEmpty()) {
                    for (LichsuTimkiemEntity oldQuery : existingQueries) {
                        repository.deleteSearchHistoryById(oldQuery.getId());
                    }
                }
                
                // Add the new query
                addSearchQuery(taikhoanId, query);
            }
        }).start();
    }
}
