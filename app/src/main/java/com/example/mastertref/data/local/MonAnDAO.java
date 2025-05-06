package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface MonAnDAO {
    // 🟢 Thêm món ăn mới và trả về ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMonAn(MonAnEntity monAn);

    // 🟢 Cập nhật món ăn
    @Update
    void updateMonAn(MonAnEntity monAn);

    // 🟢 Xóa món ăn
    @Delete
    void deleteMonAn(MonAnEntity monAn);

    // 🟢 Xóa món ăn theo ID
    @Query("DELETE FROM monan WHERE id = :monAnId")
    void deleteMonAnById(int monAnId);

    // 🟢 Lấy món ăn theo ID
    @Query("SELECT * FROM monan WHERE id = :monAnId LIMIT 1")
    LiveData<MonAnEntity> getMonAnById(int monAnId);

    @Transaction
    @Query("SELECT * FROM monan WHERE id = :monAnId")
    LiveData<MonAnWithChiTiet> getMonAnWithChiTietById(int monAnId);


    // 🟢 Lấy tất cả món ăn của một tài khoản cụ thể
    @Query("SELECT monan.* FROM monan " +
            "INNER JOIN taikhoan ON monan.taikhoan_id = taikhoan.id " +
            "WHERE taikhoan.username = :username")
    LiveData<List<MonAnEntity>> getMonAnByUsername(String username);

    // Lấy tất cả món ăn của một tài khoản (đã active)
    @Query("SELECT * FROM monan WHERE taikhoan_id = :taikhoanId AND is_active = 1 ORDER BY create_at DESC")
    List<MonAnEntity> getMonAnByTaiKhoan(int taikhoanId);

    // 🟢 Lấy tất cả món ăn (không lọc theo tài khoản)
    @Query("SELECT * FROM monan")
    LiveData<List<MonAnEntity>> getAllMonAn();

    @Transaction
    @Query("SELECT monan.* FROM monan " +
            "INNER JOIN taikhoan ON monan.taikhoan_id = taikhoan.id " +
            "WHERE taikhoan.username = :username AND monan.is_active = 1 " +
            "ORDER BY monan.create_at DESC")
    LiveData<List<MonAnWithChiTiet>> getMonAnWithChiTietByUsername(String username);






    // Lấy danh sách tất cả món ăn kèm nguyên liệu + bước nấu của 1 tài khoản
    @Transaction
    @Query("SELECT * FROM monan WHERE taikhoan_id = :taikhoanId AND is_active = 1 ORDER BY create_at DESC")
    List<MonAnWithChiTiet> getTatCaMonAnWithChiTiet(int taikhoanId);

    @Transaction
    @Query("SELECT * FROM monan")
    List<MonAnWithChiTiet> getAllMonAnWithChiTiet();


    // 🟢 Tìm món ăn theo tên (dành cho tìm kiếm)
    @Query("SELECT * FROM monan WHERE ten_monan LIKE '%' || :keyword || '%'")
    LiveData<List<MonAnEntity>> searchMonAnByName(String keyword);

    // 🟢 Lấy các món ăn đang được bật (isActive = true)
    @Query("SELECT * FROM monan WHERE is_active = 1")
    LiveData<List<MonAnEntity>> getActiveMonAn();

    // 🟢 Bật/tắt trạng thái hoạt động của món ăn
    @Query("UPDATE monan SET is_active = :active WHERE id = :monAnId")
    void setMonAnActiveStatus(int monAnId, boolean active);
}
