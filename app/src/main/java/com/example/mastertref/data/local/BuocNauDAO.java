package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BuocNauDAO {

    @Insert
    void insertBuocNau(BuocNauEntity buocNau);

    @Insert
    void insertBuocNauList(List<BuocNauEntity> list);

    @Query("SELECT * FROM buocnau WHERE monan_id = :monAnId ORDER BY so_buoc ASC")
    List<BuocNauEntity> getBuocNauByMonAnId(int monAnId);

    @Update
    void updateBuocNau(BuocNauEntity buocNau);

    @Delete
    void deleteBuocNau(BuocNauEntity buocNau);

    @Query("DELETE FROM buocnau WHERE monan_id = :monAnId")
    void deleteAllByMonAnId(int monAnId);
}
