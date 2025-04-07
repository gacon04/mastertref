package com.example.mastertref.data.local;

import androidx.room.*;

import java.util.List;

@Dao
public interface LichsuTimkiemDAO {
    @Insert
    void insertHistory(LichsuTimkiemEntity history);
    @Query("SELECT * FROM lichsu_timkiem WHERE user_id = :userId ORDER BY search_date DESC LIMIT 10")
    List<LichsuTimkiemEntity> getRecentSearches(int userId);

    @Query("DELETE FROM lichsu_timkiem WHERE id = :historyId")
    void deleteSearchHistory(int historyId);

    @Query("DELETE FROM lichsu_timkiem WHERE user_id = :userId")
    void clearHistory(int userId);
}
