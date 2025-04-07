package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;
import androidx.room.ColumnInfo;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "monan",
        foreignKeys = @ForeignKey(
                entity = TaikhoanEntity.class,
                parentColumns = "id",
                childColumns = "taikhoan_id",
                onDelete = CASCADE
        )
)
public class MonAnEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "taikhoan_id", index = true)
    private int taikhoanId; // Liên kết với tài khoản

    @ColumnInfo(name = "ten_monan")
    private String tenMonAn;

    @ColumnInfo(name = "thoi_gian")
    private String thoiGian;
    @ColumnInfo(name = "mo_ta")
    private String moTa;

    @ColumnInfo(name = "hinh_anh")
    private String hinhAnh;

    public MonAnEntity(int taikhoanId, String tenMonAn, String thoiGian, String moTa, String hinhAnh) {
        this.taikhoanId = taikhoanId;
        this.tenMonAn = tenMonAn;
        this.thoiGian = thoiGian;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTaikhoanId() { return taikhoanId; }
    public void setTaikhoanId(int taikhoanId) { this.taikhoanId = taikhoanId; }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getTenMonAn() { return tenMonAn; }
    public void setTenMonAn(String tenMonAn) { this.tenMonAn = tenMonAn; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}
