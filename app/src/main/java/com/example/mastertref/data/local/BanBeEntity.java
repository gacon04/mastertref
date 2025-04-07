package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "banbe",
        foreignKeys = {
                @ForeignKey(entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "friend_id",
                        onDelete = CASCADE)
        }
)
public class BanBeEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id", index = true)
    private int userId; // Người bấm kết bạn

    @ColumnInfo(name = "friend_id", index = true)
    private int friendId; // Người được kết bạn

    public BanBeEntity(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFriendId() { return friendId; }
    public void setFriendId(int friendId) { this.friendId = friendId; }
}
