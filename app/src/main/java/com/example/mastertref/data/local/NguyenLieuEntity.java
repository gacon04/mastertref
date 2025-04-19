package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "nguyenlieu",
        foreignKeys = @ForeignKey(
                entity = MonAnEntity.class,
                parentColumns = "id",
                childColumns = "monan_id",
                onDelete = CASCADE
        )
)
public class NguyenLieuEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "monan_id", index = true)
    private int monanId; // Liên kết với món ăn

    @ColumnInfo(name = "ten_nguyen_lieu")
    private String tenNguyenLieu;

    @ColumnInfo(name = "dinh_luong")
    private String dinhLuong; // Ví dụ: "300 gram", "2 cành", "1 trái"...

    public NguyenLieuEntity(int monanId, String tenNguyenLieu, String dinhLuong) {
        this.monanId = monanId;
        this.tenNguyenLieu = tenNguyenLieu;
        this.dinhLuong = dinhLuong;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMonanId() { return monanId; }
    public void setMonanId(int monanId) { this.monanId = monanId; }

    public String getTenNguyenLieu() { return tenNguyenLieu; }
    public void setTenNguyenLieu(String tenNguyenLieu) { this.tenNguyenLieu = tenNguyenLieu; }

    public String getDinhLuong() { return dinhLuong; }
    public void setDinhLuong(String dinhLuong) { this.dinhLuong = dinhLuong; }
}
