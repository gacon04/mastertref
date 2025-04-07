package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CamXucDAO {
    @Insert
    void insertEmotion(CamXucEntity emotion);

    @Query("SELECT COUNT(*) FROM cam_xuc WHERE mon_id = :monId AND loai_cam_xuc = 1")
    int getCountSmile(int monId); // 😋

    @Query("SELECT COUNT(*) FROM cam_xuc WHERE mon_id = :monId AND loai_cam_xuc = 2")
    int getCountHeart(int monId); // ❤️

    @Query("SELECT COUNT(*) FROM cam_xuc WHERE mon_id = :monId AND loai_cam_xuc = 3")
    int getCountClap(int monId); // 👏

    @Query("DELETE FROM cam_xuc WHERE user_id = :userId AND mon_id = :monId")
    void removeEmotion(int userId, int monId);
}

