package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.*;

import java.util.List;

@Dao
public interface DaXemGanDayDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DaXemGanDayEntity entity);

    // Modified query to exclude food items owned by the user
    @Query("SELECT d.* FROM daxemganday d JOIN monan m ON d.mon_an_id = m.id " +
           "WHERE d.user_id = :userId AND m.taikhoan_id != :userId " +
           "GROUP BY d.mon_an_id ORDER BY MAX(d.thoi_gian_xem) DESC LIMIT 4")
    List<DaXemGanDayEntity> getRecentViews(int userId);
    
    // New query to get MonAnWithChiTiet objects for recently viewed recipes
    @Transaction
    @Query("SELECT m.* FROM monan m JOIN " +
           "(SELECT d.mon_an_id, MAX(d.thoi_gian_xem) as max_time FROM daxemganday d " +
           "WHERE d.user_id = :userId " +
           "GROUP BY d.mon_an_id ORDER BY max_time DESC LIMIT 4) as recent " +
           "ON m.id = recent.mon_an_id " +
           "WHERE m.taikhoan_id != :userId")
    List<MonAnWithChiTiet> getRecentViewsWithDetails(int userId);
    
    @Query("DELETE FROM daxemganday WHERE id = :id")
    void deleteById(int id);
    
    @Query("DELETE FROM daxemganday WHERE user_id = :userId")
    void deleteAllByUserId(int userId);
    
    @Query("SELECT * FROM daxemganday WHERE user_id = :userId AND mon_an_id = :monAnId LIMIT 1")
    DaXemGanDayEntity findByUserAndMonAn(int userId, int monAnId);
}

