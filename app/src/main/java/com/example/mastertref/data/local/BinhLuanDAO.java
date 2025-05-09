package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import com.example.mastertref.data.local.BinhLuanEntity;
import com.example.mastertref.data.local.BinhLuanTaiKhoanEntity;

import java.util.List;

@Dao
public interface BinhLuanDAO {

    // Thêm bình luận mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBinhLuan(BinhLuanEntity binhLuan);

    // Cập nhật bình luận
    @Update
    void updateBinhLuan(BinhLuanEntity binhLuan);

    // Xóa bình luận
    @Delete
    void deleteBinhLuan(BinhLuanEntity binhLuan);
    @Transaction
    @Query("SELECT * FROM binh_luan")
    LiveData<List<BinhLuanTaiKhoanEntity>> getAllBinhLuansWithUser();

    @Transaction
    @Query("SELECT * FROM binh_luan WHERE mon_id = :monId ORDER BY thoi_gian DESC")
    LiveData<List<BinhLuanTaiKhoanEntity>> getBinhLuansWithUserByMonId(int monId);

    // Lấy tất cả bình luận
    @Query("SELECT * FROM binh_luan ORDER BY thoi_gian DESC")
    LiveData<List<BinhLuanEntity>> getAllBinhLuans();

    // Lấy bình luận theo món ăn
    @Query("SELECT * FROM binh_luan WHERE mon_id = :monId ORDER BY thoi_gian DESC")
    LiveData<List<BinhLuanEntity>> getBinhLuansByMonId(int monId);

    // Lấy bình luận theo người dùng
    @Query("SELECT * FROM binh_luan WHERE user_id = :userId ORDER BY thoi_gian DESC")
    LiveData<List<BinhLuanEntity>> getBinhLuansByUserId(int userId);

    // Lấy bình luận theo id
    @Query("SELECT * FROM binh_luan WHERE id = :id")
    LiveData<BinhLuanEntity> getBinhLuanById(int id);

}
