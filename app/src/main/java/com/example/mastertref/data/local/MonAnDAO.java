package com.example.mastertref.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface MonAnDAO {
    // Thêm món ăn mới và trả về ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMonAn(MonAnEntity monAn);

    // Cập nhật món ăn
    @Update
    void updateMonAn(MonAnEntity monAn);

    //  Xóa món ăn
    @Delete
    void deleteMonAn(MonAnEntity monAn);

    //  Xóa món ăn theo ID
    @Query("DELETE FROM monan WHERE id = :monAnId")
    void deleteMonAnById(int monAnId);

    //  Lấy món ăn theo ID
    @Query("SELECT * FROM monan WHERE id = :monAnId LIMIT 1")
    LiveData<MonAnEntity> getMonAnById(int monAnId);

    @Transaction
    @Query("SELECT * FROM monan WHERE id = :monAnId")
    LiveData<MonAnWithChiTiet> getMonAnWithChiTietById(int monAnId);


    //  Lấy tất cả món ăn của một tài khoản cụ thể
    @Query("SELECT monan.* FROM monan " +
            "INNER JOIN taikhoan ON monan.taikhoan_id = taikhoan.id " +
            "WHERE taikhoan.username = :username")
    LiveData<List<MonAnEntity>> getMonAnByUsername(String username);

    // Lấy tất cả món ăn của một tài khoản (đã active)
    @Query("SELECT * FROM monan WHERE taikhoan_id = :taikhoanId AND is_active = 1 ORDER BY create_at DESC")
    List<MonAnEntity> getMonAnByTaiKhoan(int taikhoanId);


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
    LiveData<List<MonAnWithChiTiet>> searchMonAnByName(String keyword);

    // Tìm kiếm món ăn theo nguyên liệu
    @Query("SELECT DISTINCT m.* FROM monan m " +
           "INNER JOIN nguyenlieu n ON m.id = n.monan_id " +
           "WHERE n.ten_nguyen_lieu LIKE '%' || :keyword || '%'")
    LiveData<List<MonAnWithChiTiet>> searchMonAnByIngredient(String keyword);

    // Tìm kiếm món ăn theo tên hoặc nguyên liệu, chỉ từ tài khoản đang active và không bị chặn
    @Query("SELECT DISTINCT m.* FROM monan m " +
           "LEFT JOIN nguyenlieu n ON m.id = n.monan_id " +
           "INNER JOIN taikhoan t ON m.taikhoan_id = t.id " +
           "WHERE (m.ten_monan LIKE '%' || :keyword || '%' OR n.ten_nguyen_lieu LIKE '%' || :keyword || '%') " +
           "AND t.isActive = 1 " +
           "AND m.is_active = 1 " +
           "AND NOT EXISTS (SELECT 1 FROM chantaikhoan c WHERE c.blocker_id = :currentUserId AND c.blocked_id = m.taikhoan_id)")
    LiveData<List<MonAnWithChiTiet>> searchMonAnByNameOrIngredient(String keyword, int currentUserId);

    // Tìm kiếm món ăn theo tên hoặc nguyên liệu (không cần lọc người dùng bị chặn)
    @Query("SELECT DISTINCT m.* FROM monan m " +
           "LEFT JOIN nguyenlieu n ON m.id = n.monan_id " +
           "INNER JOIN taikhoan t ON m.taikhoan_id = t.id " +
           "WHERE (m.ten_monan LIKE '%' || :keyword || '%' OR n.ten_nguyen_lieu LIKE '%' || :keyword || '%') " +
           "AND t.isActive = 1 " +
           "AND m.is_active = 1")
    LiveData<List<MonAnWithChiTiet>> searchMonAnByNameOrIngredient(String keyword);

    // 🟢 Lấy các món ăn đang được bật (isActive = true)
    @Query("SELECT * FROM monan WHERE is_active = 1")
    LiveData<List<MonAnEntity>> getActiveMonAn();

    // 🟢 Bật/tắt trạng thái hoạt động của món ăn
    @Query("UPDATE monan SET is_active = :active WHERE id = :monAnId")
    void setMonAnActiveStatus(int monAnId, boolean active);

    @Transaction
    @Query("SELECT DISTINCT m.* FROM monan m " +
            "INNER JOIN nguyenlieu n1 ON m.id = n1.monan_id " +
            "WHERE n1.ten_nguyen_lieu IN (" +
            "    SELECT n2.ten_nguyen_lieu FROM nguyenlieu n2 WHERE n2.monan_id = :sourceMonAnId" +
            ") AND m.id != :sourceMonAnId AND m.is_active = 1 " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(DISTINCT n1.ten_nguyen_lieu) DESC " +
            "LIMIT :limit")
    LiveData<List<MonAnWithChiTiet>> getSimilarRecipesByIngredients(int sourceMonAnId, int limit);

    // Lấy các món ăn cùng tác giả
    @Transaction
    @Query("SELECT m.* FROM monan m " +
            "WHERE m.taikhoan_id = (" +
            "    SELECT m2.taikhoan_id FROM monan m2 WHERE m2.id = :sourceMonAnId" +
            ") AND m.id != :sourceMonAnId AND m.is_active = 1 " +
            "ORDER BY m.create_at DESC " +
            "LIMIT :limit")
    LiveData<List<MonAnWithChiTiet>> getRecipesBySameAuthor(int sourceMonAnId, int limit);

    // Lấy các món ăn ngẫu nhiên (khi không có đủ món ăn tương tự)
    @Transaction
    @Query("SELECT * FROM monan " +
            "WHERE id != :sourceMonAnId AND is_active = 1 " +
            "ORDER BY RANDOM() " +
            "LIMIT :limit")
    LiveData<List<MonAnWithChiTiet>> getRandomRecipesExcept(int sourceMonAnId, int limit);

    // Lấy các món ăn tương tự kết hợp (ưu tiên món có nguyên liệu chung, sau đó là cùng tác giả, sau cùng là ngẫu nhiên)

    @Transaction
    @Query("SELECT DISTINCT m.* FROM monan m " +
            "LEFT JOIN nguyenlieu n1 ON m.id = n1.monan_id " +
            "LEFT JOIN nguyenlieu n2 ON n2.monan_id = :sourceMonAnId " +
            "WHERE m.id != :sourceMonAnId AND m.is_active = 1 " +
            "GROUP BY m.id " +
            "HAVING COUNT(CASE WHEN n1.ten_nguyen_lieu = n2.ten_nguyen_lieu THEN 1 ELSE NULL END) >= 0 " +
            "ORDER BY " +
            "CASE WHEN m.taikhoan_id != (SELECT taikhoan_id FROM monan WHERE id = :sourceMonAnId) THEN 1 ELSE 0 END DESC, " +
            "COUNT(CASE WHEN n1.ten_nguyen_lieu = n2.ten_nguyen_lieu THEN 1 ELSE NULL END) DESC, " +
            "CASE WHEN COUNT(CASE WHEN n1.ten_nguyen_lieu = n2.ten_nguyen_lieu THEN 1 ELSE NULL END) > 0 THEN 1 ELSE 0 END DESC, " +
            "RANDOM() " +
            "LIMIT :limit")
    LiveData<List<MonAnWithChiTiet>> getRecommendedRecipes(int sourceMonAnId, int limit);



    // Lấy các món ăn mới nhất, không bao gồm người dùng bị chặn hoặc đã chặn người dùng hiện tại
    @Transaction
    @Query("SELECT m.* FROM monan m " +
           "INNER JOIN taikhoan t ON m.taikhoan_id = t.id " +
           "WHERE m.is_active = 1 " +
           "AND t.isActive = 1 " +
            "AND NOT EXISTS (SELECT 1 FROM chantaikhoan c WHERE (c.blocker_id = :currentUserId AND c.blocked_id = m.taikhoan_id) " +
           "OR (c.blocker_id = m.taikhoan_id AND c.blocked_id = :currentUserId)) " +
           "ORDER BY m.create_at DESC " +
           "LIMIT :limit")
    LiveData<List<MonAnWithChiTiet>> getNewestRecipesFiltered(int currentUserId, int limit);
}