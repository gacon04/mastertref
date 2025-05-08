package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(
        tableName = "chantaikhoan",
        foreignKeys = {
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "blocker_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "blocked_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class ChanTaiKhoanEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "blocker_id", index = true)
    private int blockerId; // Người đã chặn

    @ColumnInfo(name = "blocked_id", index = true)
    private int blockedId; // Người bị chặn

    // Constructor
    public ChanTaiKhoanEntity(int blockerId, int blockedId) {
        this.blockerId = blockerId;
        this.blockedId = blockedId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(int blockerId) {
        this.blockerId = blockerId;
    }

    public int getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(int blockedId) {
        this.blockedId = blockedId;
    }
}
