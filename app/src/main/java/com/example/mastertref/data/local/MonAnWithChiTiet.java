package com.example.mastertref.data.local;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Lớp này biểu diễn một món ăn kèm theo danh sách nguyên liệu và bước nấu.
 */
public class MonAnWithChiTiet {

    @Embedded
    private MonAnEntity monAn;

    @Relation(
            parentColumn = "id",
            entityColumn = "monan_id"
    )
    private List<NguyenLieuEntity> nguyenLieuList;

    @Relation(
            parentColumn = "id",
            entityColumn = "monan_id"
    )
    private List<BuocNauEntity> buocNauList;

    // Getters & Setters
    public MonAnEntity getMonAn() {
        return monAn;
    }

    public void setMonAn(MonAnEntity monAn) {
        this.monAn = monAn;
    }

    public List<NguyenLieuEntity> getNguyenLieuList() {
        return nguyenLieuList;
    }

    public void setNguyenLieuList(List<NguyenLieuEntity> nguyenLieuList) {
        this.nguyenLieuList = nguyenLieuList;
    }

    public List<BuocNauEntity> getBuocNauList() {
        return buocNauList;
    }

    public void setBuocNauList(List<BuocNauEntity> buocNauList) {
        this.buocNauList = buocNauList;
    }
}
