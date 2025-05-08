package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TheoDoiTaiKhoanDAO {

    // 🟢 Thêm một lượt theo dõi
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TheoDoiTaiKhoanEntity theoDoi);

    // 🟢 Hủy theo dõi
    @Query("DELETE FROM theodoitaikhoan WHERE follower_id = :followerId AND following_id = :followingId")
    void unfollow(int followerId, int followingId);

    // 🟢 Kiểm tra đã theo dõi chưa
    @Query("SELECT COUNT(*) FROM theodoitaikhoan WHERE follower_id = :followerId AND following_id = :followingId")
    int isFollowing(int followerId, int followingId);

    // 🟢 Lấy danh sách người dùng mà user này đang theo dõi
    @Query("SELECT following_id FROM theodoitaikhoan WHERE follower_id = :followerId")
    LiveData<List<Integer>> getFollowingUserIds(int followerId);

    // 🟢 Lấy danh sách người đang theo dõi user này
    @Query("SELECT follower_id FROM theodoitaikhoan WHERE following_id = :userId")
    LiveData<List<Integer>> getFollowerUserIds(int userId);
}
