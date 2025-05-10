package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface LichsuTimkiemDAO {
    @Insert
    void insertHistory(LichsuTimkiemEntity history);

    @Query("SELECT * FROM lichsu_timkiem WHERE user_id = :userId ORDER BY search_date DESC LIMIT 10")
    LiveData<List<LichsuTimkiemEntity>> getRecentSearches(int userId);

    @Query("DELETE FROM lichsu_timkiem WHERE id = :historyId")
    void deleteSearchHistory(int historyId);

    @Query("DELETE FROM lichsu_timkiem WHERE user_id = :userId")
    void clearHistory(int userId);

    @Query("SELECT * FROM lichsu_timkiem WHERE user_id = :userId AND keyword = :keyword LIMIT 1")
    LichsuTimkiemEntity getExistingKeyword(int userId, String keyword);

    @Query("DELETE FROM lichsu_timkiem WHERE user_id = :userId AND id NOT IN (SELECT id FROM lichsu_timkiem WHERE user_id = :userId ORDER BY search_date DESC LIMIT 10)")
    void trimHistoryToMaxTen(int userId);
}

