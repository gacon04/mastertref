package com.example.mastertref.data.local;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "daxemganday",
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
public class DaXemGanDayEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "mon_an_id")
    private int monAnId;

    @ColumnInfo(name = "thoi_gian_xem")
    private long thoiGianXem; // thời điểm xem, kiểu timestamp

    // Constructor
    public DaXemGanDayEntity(int userId, int monAnId, long thoiGianXem) {
        this.userId = userId;
        this.monAnId = monAnId;
        this.thoiGianXem = thoiGianXem;
    }

    // Getters and Setters
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

    public long getThoiGianXem() {
        return thoiGianXem;
    }

    public void setThoiGianXem(long thoiGianXem) {
        this.thoiGianXem = thoiGianXem;
    }
}
