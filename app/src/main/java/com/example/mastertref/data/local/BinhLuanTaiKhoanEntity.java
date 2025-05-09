package com.example.mastertref.data.local;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.Date;

public class BinhLuanTaiKhoanEntity {
    @Embedded
    private BinhLuanEntity binhLuan;

    @Relation(
            parentColumn = "user_id",
            entityColumn = "id"
    )
    private TaikhoanEntity taiKhoan;

    // Constructor
    public BinhLuanTaiKhoanEntity(BinhLuanEntity binhLuan, TaikhoanEntity taiKhoan) {
        this.binhLuan = binhLuan;
        this.taiKhoan = taiKhoan;
    }

    // Getter & Setter
    public BinhLuanEntity getBinhLuan() {
        return binhLuan;
    }

    public void setBinhLuan(BinhLuanEntity binhLuan) {
        this.binhLuan = binhLuan;
    }

    public TaikhoanEntity getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaikhoanEntity taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    // Các phương thức tiện ích để truy cập dữ liệu
    public int getId() {
        return binhLuan.getId();
    }

    public String getNoiDung() {
        return binhLuan.getNoiDung();
    }

    public Date getThoiGian() {
        return binhLuan.getThoiGian();
    }

    public String getHinhAnh() {
        return binhLuan.getHinhAnh();
    }

    public String getUsername() {
        return taiKhoan.getUsername();
    }

    public String getFullname() {
        return taiKhoan.getFullname();
    }

    public String getAvatar() {
        return taiKhoan.getAvatar();
    }
}