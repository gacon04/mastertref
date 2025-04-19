package com.example.mastertref.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NguyenLieuDAO {

    @Insert
    void insertNguyenLieu(NguyenLieuEntity nguyenLieu);

    @Insert
    void insertNguyenLieuList(List<NguyenLieuEntity> list);


    @Query("SELECT * FROM nguyenlieu WHERE monan_id = :monAnId")
    List<NguyenLieuEntity> getNguyenLieuByMonAnId(int monAnId);

    @Update
    void updateNguyenLieu(NguyenLieuEntity nguyenLieu);

    @Delete
    void deleteNguyenLieu(NguyenLieuEntity nguyenLieu);

    @Query("DELETE FROM nguyenlieu WHERE monan_id = :monAnId")
    void deleteAllByMonAnId(int monAnId);
}
