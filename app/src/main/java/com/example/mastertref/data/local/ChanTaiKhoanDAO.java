package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChanTaiKhoanDAO {

    // 🟢 Thêm một lượt chặn
    @Insert
    void insertBlock(ChanTaiKhoanEntity block);

    // 🔴 Bỏ chặn
    @Delete
    void deleteBlock(ChanTaiKhoanEntity block);

    // 🟡 Bỏ chặn bằng ID hai người
    @Query("DELETE FROM chantaikhoan WHERE blocker_id = :blockerId AND blocked_id = :blockedId")
    void deleteBlockByUserIds(int blockerId, int blockedId);

    // 🔎 Kiểm tra người A có chặn người B không
    @Query("SELECT COUNT(*) FROM chantaikhoan WHERE blocker_id = :blockerId AND blocked_id = :blockedId")
    int isBlocked(int blockerId, int blockedId);

    // 📋 Lấy danh sách người bị chặn bởi một user
    @Query("SELECT blocked_id FROM chantaikhoan WHERE blocker_id = :blockerId")
    LiveData<List<Integer>> getBlockedUserIds(int blockerId);

    // 📋 Lấy danh sách người đã chặn user này
    @Query("SELECT blocker_id FROM chantaikhoan WHERE blocked_id = :blockedId")
    LiveData<Integer> getWhoBlockedUser(int blockedId);

    @Query("SELECT COUNT(*) > 0 FROM chantaikhoan WHERE blocker_id = :userId AND blocked_id = :blockedUserId")
    boolean isUserBlocked(int userId, int blockedUserId);
}
