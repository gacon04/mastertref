package com.example.mastertref.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "monan_daluu",
        foreignKeys = {
                @ForeignKey(
                        entity = TaikhoanEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MonAnEntity.class,
                        parentColumns = "id",
                        childColumns = "mon_an_id",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index(value = "user_id"),
                @Index(value = "mon_an_id")
        }
)
public class MonAnDaLuuEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "mon_an_id")
    private int monAnId;

    @ColumnInfo(name = "thoi_gian_luu")
    private long thoiGianLuu; // thời điểm người dùng lưu món ăn

    // Constructor
    public MonAnDaLuuEntity(int userId, int monAnId, long thoiGianLuu) {
        this.userId = userId;
        this.monAnId = monAnId;
        this.thoiGianLuu = thoiGianLuu;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMonAnId() {
        return monAnId;
    }

    public void setMonAnId(int monAnId) {
        this.monAnId = monAnId;
    }

    public long getThoiGianLuu() {
        return thoiGianLuu;
    }

    public void setThoiGianLuu(long thoiGianLuu) {
        this.thoiGianLuu = thoiGianLuu;
    }
}
