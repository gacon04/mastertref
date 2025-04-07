package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface BanBeDAO {
    @Insert
    void addFriend(BanBeEntity banBe);

    @Query("SELECT friend_id FROM BanBeEntity WHERE user_id = :userId")
    List<Integer> getFriends(int userId);

    @Query("SELECT COUNT(*) FROM BanBeEntity WHERE user_id = :userId AND friend_id = :friendId")
    int isFriend(int userId, int friendId);

    @Transaction
    default void addFriendship(int userId, int friendId) {
        if (isFriend(userId, friendId) == 0) { // Kiểm tra xem đã là bạn chưa
            addFriend(new BanBeEntity(userId, friendId));
            addFriend(new BanBeEntity(friendId, userId)); // Thêm dòng ngược lại
        }
    }
}
