package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface LichsuTimkiemDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LichsuTimkiemEntity timKiem);

    @Query("SELECT * FROM tim_kiem WHERE taikhoanId = :taikhoanId GROUP BY tuKhoa ORDER BY thoiGian DESC LIMIT :limit")
    LiveData<List<LichsuTimkiemEntity>> getSearchHistoryByUser(int taikhoanId, int limit);

    @Query("DELETE FROM tim_kiem WHERE taikhoanId = :taikhoanId")
    void deleteAllSearchHistoryByUser(int taikhoanId);

    @Query("DELETE FROM tim_kiem WHERE id = :id")
    void deleteSearchHistoryById(int id);

    @Query("SELECT * FROM tim_kiem WHERE taikhoanId = :taikhoanId ORDER BY thoiGian DESC LIMIT :limit")
    List<LichsuTimkiemEntity> getSearchHistoryByUserSync(int taikhoanId, int limit);

    @Query("SELECT * FROM tim_kiem WHERE taikhoanId = :taikhoanId AND tuKhoa LIKE :query")
    List<LichsuTimkiemEntity> searchInHistorySync(int taikhoanId, String query);

    @Query("SELECT * FROM tim_kiem WHERE taikhoanId = :taikhoanId AND tuKhoa LIKE '%' || :query || '%' ORDER BY thoiGian DESC")
    LiveData<List<LichsuTimkiemEntity>> searchInHistory(int taikhoanId, String query);
}



