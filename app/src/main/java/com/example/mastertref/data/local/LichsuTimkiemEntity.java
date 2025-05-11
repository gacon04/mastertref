package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tim_kiem",
        foreignKeys = @ForeignKey(
                entity = TaikhoanEntity.class,
                parentColumns = "id",
                childColumns = "taikhoanId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("taikhoanId")}
)
public class LichsuTimkiemEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int taikhoanId;
    private String tuKhoa;
    private long thoiGian; // Thời gian tìm kiếm (timestamp)

    public LichsuTimkiemEntity(int taikhoanId, String tuKhoa, long thoiGian) {
        this.taikhoanId = taikhoanId;
        this.tuKhoa = tuKhoa;
        this.thoiGian = thoiGian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaikhoanId() {
        return taikhoanId;
    }

    public void setTaikhoanId(int taikhoanId) {
        this.taikhoanId = taikhoanId;
    }

    public String getTuKhoa() {
        return tuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        this.tuKhoa = tuKhoa;
    }

    public long getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(long thoiGian) {
        this.thoiGian = thoiGian;
    }
}