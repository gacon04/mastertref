package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.*;

import java.util.List;

@Dao
public interface DaXemGanDayDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DaXemGanDayEntity entity);

    @Query("SELECT * FROM daxemganday WHERE user_id = :userId ORDER BY thoi_gian_xem DESC LIMIT 10")
    List<DaXemGanDayEntity> getRecentViews(int userId);
}

