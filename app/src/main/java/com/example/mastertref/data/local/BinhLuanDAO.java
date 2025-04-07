package com.example.mastertref.data.local;

import androidx.room.*;

import java.util.List;

@Dao
public interface BinhLuanDAO {
    @Insert
    void insertComment(BinhLuanEntity comment);

    @Query("SELECT * FROM binh_luan WHERE mon_id = :monId ORDER BY thoi_gian DESC")
    List<BinhLuanEntity> getCommentsByDish(int monId);

    @Query("DELETE FROM binh_luan WHERE id = :commentId")
    void deleteComment(int commentId);

    @Query("DELETE FROM binh_luan WHERE user_id = :userId")
    void deleteCommentsByUser(int userId);
}

