package com.example.mastertref.data.local;

import androidx.room.Entity;
import androidx.room.Ignore;
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
    @ColumnInfo(name = "khau_phan")
    private String khauPhan;
    @ColumnInfo(name = "hinh_anh")
    private String hinhAnh;
    @ColumnInfo(name = "is_active")
    private boolean isActive;
    @ColumnInfo(name = "create_at")
    private long createAt;
    @ColumnInfo(name= "update_at")
    private long updateAt;

    public MonAnEntity(String tenMonAn, String moTa, String khauPhan, String thoiGian) {
        this.tenMonAn = tenMonAn;
        this.moTa = moTa;
        this.khauPhan = khauPhan;
        this.thoiGian = thoiGian;
    }
    @Ignore
    public MonAnEntity(int taikhoanId, String tenMonAn, String thoiGian, String moTa, String khauPhan, String hinhAnh, boolean isActive, long createAt, long updateAt) {
        this.taikhoanId = taikhoanId;
        this.tenMonAn = tenMonAn;
        this.thoiGian = thoiGian;
        this.moTa = moTa;
        this.khauPhan = khauPhan;
        this.hinhAnh = hinhAnh;
        this.isActive = isActive;
        this.createAt = createAt;
        this.updateAt = updateAt;
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

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public long getCreateAt() {
        return createAt;
    }
    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
    public String getKhauPhan() {
        return khauPhan;
    }
    public void setKhauPhan(String khauPhan) {
        this.khauPhan = khauPhan;
    }
    public long getUpdateAt() {
        return updateAt;
    }
    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

}
